;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname |problem set 8p2|) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")) #f)))
;; Duk Hwan Kim, Tina Lee
;; Problem Set 8
;; Tetris

(require 2htdp/image)
(require 2htdp/universe)
(require racket/list)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; GLOBALS

(define WORLD-WIDTH 10) ;in grid units
(define WORLD-HEIGHT 20) ;in grid units 
(define GRID-SIZE 20) ;in pixels
(define BACKGROUND (empty-scene (* GRID-SIZE WORLD-WIDTH)
                                (* GRID-SIZE WORLD-HEIGHT)))
(define T-TETRA-COLOR 'gold)
(define L-TETRA-COLOR 'darkviolet)
(define O-TETRA-COLOR 'green)
(define J-TETRA-COLOR 'cyan)
(define I-TETRA-COLOR 'blue)
(define S-TETRA-COLOR 'red)
(define Z-TETRA-COLOR 'magenta)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Data Definitions


(define-struct block (x y color))
;; A Block is a (make-block Number Number Color)
;; (x,y) is the bottom left corner of the block

;; A Set of Blocks (BSet) is one of:
;; - empty
;; - (cons Block BSet)
;; Order does not matter

(define-struct tetra (center blocks))
;; A Tetra is a (make-tetra Posn BSet)
;; The center point is the point around which the tetra rotates when it spins

(define-struct world (tetra pile))
;; A World is a (make-world Tetra BSet)
;; The BSet represents the pile of blocks at the bottom of the screen

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Tetra Blocks


;; X-TETRA - Number Number -> BSet
;; takes the coordinates of bottom-left block of an 
;; X-Tetra and makes BSet of the X-Tetra

(define (o-tetra x y)
  (list (make-block    x       y    O-TETRA-COLOR) 
        (make-block (+ x 1)    y    O-TETRA-COLOR) 
        (make-block    x    (+ y 1) O-TETRA-COLOR) 
        (make-block (+ x 1) (+ y 1) O-TETRA-COLOR)))
(check-expect (o-tetra 5 15) (list (make-block 5 15 O-TETRA-COLOR)
                                   (make-block 6 15 O-TETRA-COLOR)
                                   (make-block 5 16 O-TETRA-COLOR)
                                   (make-block 6 16 O-TETRA-COLOR)))

(define (i-tetra x y)
  (list (make-block    x       y    I-TETRA-COLOR)
        (make-block (+ x 1)    y    I-TETRA-COLOR)
        (make-block (+ x 2)    y    I-TETRA-COLOR)
        (make-block (+ x 3)    y    I-TETRA-COLOR)))
(check-expect (i-tetra 3 17) (list (make-block 3 17 I-TETRA-COLOR)
                                   (make-block 4 17 I-TETRA-COLOR)
                                   (make-block 5 17 I-TETRA-COLOR)
                                   (make-block 6 17 I-TETRA-COLOR)))

(define (l-tetra x y)
  (list (make-block    x       y    L-TETRA-COLOR)
        (make-block (+ x 1)    y    L-TETRA-COLOR)
        (make-block (+ x 2)    y    L-TETRA-COLOR)
        (make-block (+ x 2) (+ y 1) L-TETRA-COLOR)))
(check-expect (l-tetra 6 10) (list (make-block 6 10 L-TETRA-COLOR)
                                   (make-block 7 10 L-TETRA-COLOR)
                                   (make-block 8 10 L-TETRA-COLOR)
                                   (make-block 8 11 L-TETRA-COLOR)))

(define (j-tetra x y)
  (list (make-block    x       y    J-TETRA-COLOR)
        (make-block    x    (+ y 1) J-TETRA-COLOR)
        (make-block (+ x 1)    y    J-TETRA-COLOR)
        (make-block (+ x 2)    y    J-TETRA-COLOR)))
(check-expect (j-tetra 8 3) (list (make-block 8 3 J-TETRA-COLOR)
                                  (make-block 8 4 J-TETRA-COLOR)
                                  (make-block 9 3 J-TETRA-COLOR)
                                  (make-block 10 3 J-TETRA-COLOR)))

(define (t-tetra x y)
  (list (make-block    x       y    T-TETRA-COLOR)
        (make-block (+ x 1)    y    T-TETRA-COLOR)
        (make-block (+ x 1) (+ y 1) T-TETRA-COLOR)
        (make-block (+ x 2)    y    T-TETRA-COLOR)))
(check-expect (t-tetra 6 7) (list (make-block 6 7 T-TETRA-COLOR)
                                  (make-block 7 7 T-TETRA-COLOR)
                                  (make-block 7 8 T-TETRA-COLOR)
                                  (make-block 8 7 T-TETRA-COLOR)))

(define (z-tetra x y)
  (list (make-block    x    (+ y 1) Z-TETRA-COLOR)
        (make-block (+ x 1)    y    Z-TETRA-COLOR)
        (make-block (+ x 1) (+ y 1) Z-TETRA-COLOR)
        (make-block (+ x 2)    y    Z-TETRA-COLOR)))
(check-expect (z-tetra 17 14) (list (make-block 17 15 Z-TETRA-COLOR)
                                    (make-block 18 14 Z-TETRA-COLOR)
                                    (make-block 18 15 Z-TETRA-COLOR)
                                    (make-block 19 14 Z-TETRA-COLOR)))

(define (s-tetra x y)
  (list (make-block    x       y    S-TETRA-COLOR)
        (make-block (+ x 1)    y    S-TETRA-COLOR)
        (make-block (+ x 1) (+ y 1) S-TETRA-COLOR)
        (make-block (+ x 2) (+ y 1) S-TETRA-COLOR)))
(check-expect (s-tetra 10 10) (list (make-block 10 10 S-TETRA-COLOR)
                                    (make-block 11 10 S-TETRA-COLOR)
                                    (make-block 11 11 S-TETRA-COLOR)
                                    (make-block 12 11 S-TETRA-COLOR)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Data Examples for Testing

(define block1 (make-block 3 4 'red))
(define block2 (make-block 5 1 'magenta))
(define block3 (make-block 1 1 'blue))

(define bset1 (list block1 block2))

(define otetra1 (make-tetra (make-posn 6 16) (o-tetra 5 15))) 
(define otetra2 (make-tetra (make-posn 6 15) (o-tetra 5 14)))
(define ztetra1 (make-tetra (make-posn 5 17) (z-tetra 5 16)))
(define itetra1 (make-tetra (make-posn 4 0)  (i-tetra 2 0)))
(define blocktetra (make-tetra (make-posn 8 19) (list block1)))
(define toptetra (make-tetra (make-posn 7 WORLD-HEIGHT)
                             (o-tetra 7 (sub1 WORLD-HEIGHT))))

(define world1 (make-world otetra1 (list block1)))
(define world2 (make-world blocktetra (list block2)))
(define world3 (make-world ztetra1 '()))
(define world4 (make-world itetra1 '()))
(define world5 (make-world otetra1 (tetra-blocks otetra1)))
(define world6 (make-world otetra1 (append (o-tetra 0 0)
                                           (o-tetra 2 0)
                                           (o-tetra 4 0)
                                           (o-tetra 6 0)
                                           (o-tetra 8 0))))
(define world7 (make-world otetra1 (append (o-tetra 0 1)
                                           (o-tetra 2 2)
                                           (o-tetra 4 2)
                                           (o-tetra 6 2)
                                           (o-tetra 8 2))))
(define world7.1 (make-world otetra1 (list (make-block 0 1 'green)
                                        (make-block 1 1'green)
                                        (make-block 2 2 'green)
                                        (make-block 3 2 'green)
                                        (make-block 4 2 'green)
                                        (make-block 5 2 'green)
                                        (make-block 6 2 'green)
                                        (make-block 7 2 'green)
                                        (make-block 8 2 'green)
                                        (make-block 9 2 'green))))
(define world8 (make-world otetra1 (append (world-pile world6) (o-tetra 0 2))))
(define topworld (make-world otetra1 (tetra-blocks toptetra)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Boundary Functions


;; list-member? - List1 List2 -> Boolean
;; Are any elements of List1 in List2?
(define (list-member? l1 l2)
  (ormap (lambda (b) (member? b l2)) l1))
(check-expect (list-member? (list 2 4) (list 3 4)) true)
(check-expect (list-member? (list 1 2) (list 2 3)) true)
(check-expect (list-member? (list 1 2) (list 3 4)) false)

;; block-list - BSet -> [List of Posns]
;; Returns list of all block locations
(define (block-list bs)
  (map (lambda (b) (make-posn (block-x b) (block-y b))) bs))
(check-expect (block-list bset1)(list (make-posn 3 4) (make-posn 5 1)))

;; tbound-helper -
;; [List of Blocks] [Number Number -> Boolean] [Block -> Number] Block -> Block
;; 1st function: > or <
;; 2nd function: block-x or block-y
;; Returns left/right/up/down most block
(define (tbound-helper blocks func1 func2 block)
  (cond [(empty? blocks) block]
        [(func1 (func2 (first blocks)) (func2 block))
         (tbound-helper (rest blocks) func1 func2 (first blocks))]
        [else (tbound-helper (rest blocks) func1 func2 block)]))
(check-expect (tbound-helper (z-tetra 5 16) < block-x (first (z-tetra 5 16)))
              (list-ref (z-tetra 5 16) 0))

;; tetra-bound - Tetra [Number Number -> Boolean] [Block -> Number] -> Block
;; 1st function: < is used for left and down,
;;               > is used for up and right
;; 2nd function: block-x is used for left and right,
;;               block-y is used for up and down
;; Returns left/right/up/down most block
(define (tetra-bound tetra func1 func2)
  (local [(define blocks (tetra-blocks tetra))]
    (tbound-helper blocks func1 func2 (first blocks))))
(check-expect (block-x (tetra-bound ztetra1 < block-x)) 5)
(check-expect (block-y (tetra-bound ztetra1 < block-y)) 16)
(check-expect (block-y (tetra-bound ztetra1 > block-y)) 17)
(check-expect (block-x (tetra-bound ztetra1 > block-x)) 7)

;; will-out? - World -> Boolean
;; Will the tetra go out of bounds?
(define (will-out? w)
  (or (< (block-x (tetra-bound (world-tetra w) < block-x)) 0)
      (> (block-x (tetra-bound (world-tetra w) > block-x)) (sub1 WORLD-WIDTH))))
(check-expect (will-out? world4) false)
(check-expect (will-out? world3) false)
(check-expect (will-out? (make-world
                          (make-tetra
                           (make-posn 10 20) (o-tetra 10 5)) '())) true)

;; will-pile? - World -> Boolean
;; Will the tetra hit the pile?
(define (will-pile? w)
  (list-member? (block-list(tetra-blocks (world-tetra w)))
                (block-list(world-pile w))))
(check-expect (will-pile? world3) false)
(check-expect (will-pile? world5) true)

;; bottom? - World -> Boolean
;; Is tetra at bottom of scene?
(define (bottom? w)
  (zero? (block-y (tetra-bound (world-tetra w) < block-y))))
(check-expect (bottom? world4) true)
(check-expect (bottom? world3) false)

;; will-collide? - World -> Boolean
;; will the tetra go out of bounds or hit the pile?
(define (will-collide? w)
  (or (will-out? w) (will-pile? w)))
(check-expect (will-collide? world3) false)
(check-expect (will-collide? world5) true)

;; top? - World -> Boolean
;; Has the tetra stacked to the top?
(define (top? w)
  (member? WORLD-HEIGHT (map posn-y (block-list (world-pile w)))))
(check-expect (top? world3) false)
(check-expect (top? (make-world otetra1  
                                (list (make-block 5 WORLD-HEIGHT 'pink))))     
              true)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Tetra Manipulation


;; random-tetra - Function -> Tetra
;; Takes a X-tetra function and creates a tetra
(define (random-tetra fcn)
  (make-tetra (make-posn 5 21) (fcn 4 21)))

(define tetra-list (list (random-tetra o-tetra)
                         (random-tetra i-tetra)
                         (random-tetra l-tetra)
                         (random-tetra j-tetra)
                         (random-tetra t-tetra)
                         (random-tetra z-tetra)
                         (random-tetra s-tetra)))
;; new-tetra - World -> World
;; Makes a new tetra when old tetra hits bottom or pile
(define (new-tetra w)
  (clean-all (make-world (list-ref tetra-list (random 7))
                         (append (tetra-blocks (world-tetra w))
                                 (world-pile w)))))
(check-expect (member (world-tetra(new-tetra world4)) tetra-list) true) 
(check-expect (world-pile (new-tetra world4))
              (tetra-blocks (world-tetra world4)))


;; fullline-ys - Bset -> [List-of Number]
;; Returns the y-coordinates of the full lines of the pile
(define (full-line-ys bset)
  (local [(define (get-tetra bset) (list (list-ref bset 0)
                                         (list-ref bset 1)
                                         (list-ref bset 2)
                                         (list-ref bset 3)))
          (define (ys blocks) (map posn-y (block-list blocks)))
          (define pile-ys (ys bset))
          (define tetra-ys (ys (get-tetra bset)))
          (define occurz
            (map (λ (x) (list x (count (λ (y) (= y x)) pile-ys))) tetra-ys))
          (define fullies (filter (λ (l) (= WORLD-WIDTH (second l))) occurz))]
    (remove-duplicates (map (λ(t) (first t)) fullies))))
(check-expect (full-line-ys (append (o-tetra 0 0)
                                    (o-tetra 2 0)
                                    (o-tetra 4 0)
                                    (o-tetra 6 0)
                                    (o-tetra 8 1)))
              (list 1))
(check-expect (full-line-ys (world-pile world6))
              (list 0 1))
              

;; clear-line - Number World -> World
;; Clears the line at y of the pile
(define (clear-line n w)
  (make-world (world-tetra w)
              (filter (lambda (x) (not (= n (block-y x)))) (world-pile w))))
(check-expect (clear-line 3 world6) world6)
(check-expect (clear-line 0 world6)
              (make-world otetra1 (list (make-block 0 1 'green)
                                        (make-block 1 1 'green)
                                        (make-block 2 1 'green)
                                        (make-block 3 1 'green)
                                        (make-block 4 1 'green)
                                        (make-block 5 1 'green)
                                        (make-block 6 1 'green)
                                        (make-block 7 1 'green)
                                        (make-block 8 1 'green)
                                        (make-block 9 1 'green))))

;; drop-pile - Number World -> World
;; Drops the pile from y up down one unit
(define (drop-pile n w)
  (local [(define (higher? b) (> (block-y b) n))]
    (make-world (world-tetra w)
                (map (lambda (x)
                       (if (higher? x)
                           (make-block (block-x x)
                                       (sub1(block-y x))
                                       (block-color x))
                           x))
                     (world-pile w)))))
(check-expect (drop-pile 5 world6) world6)
(check-expect (drop-pile 0 (clear-line 0 world6))
              (make-world otetra1 (list (make-block 0 0 'green)
                                        (make-block 1 0 'green)
                                        (make-block 2 0 'green)
                                        (make-block 3 0 'green)
                                        (make-block 4 0 'green)
                                        (make-block 5 0 'green)
                                        (make-block 6 0 'green)
                                        (make-block 7 0 'green)
                                        (make-block 8 0 'green)
                                        (make-block 9 0 'green))))


;; clean - [List-of Number] World  [World -> World] -> World
;; clears or drops the lines in the list, based on the function input
(define (clean l w func)
  (cond [(empty? l) w]
        [else (if (equal? clear-line func)
                  (clean (rest l) (func (first l) w) func)
                  (local [(define update
                            (map (lambda (x) (if (> x (first l)) (sub1 x) x))
                                 (rest l)))]
                    (clean update (func (first l) w) func)))]))                  
(check-expect (clean '(3) world6 clear-line) world6)
(check-expect (clean '(3) world6 drop-pile) world6)
(check-expect (clean '(0) (clean '(0) world6 clear-line) drop-pile)
              (make-world otetra1 (list (make-block 0 0 'green)
                                        (make-block 1 0 'green)
                                        (make-block 2 0 'green)
                                        (make-block 3 0 'green)
                                        (make-block 4 0 'green)
                                        (make-block 5 0 'green)
                                        (make-block 6 0 'green)
                                        (make-block 7 0 'green)
                                        (make-block 8 0 'green)
                                        (make-block 9 0 'green))))
(check-expect (clean (list 0 1)
                     (clean (list 0 1) world6 clear-line)
                     drop-pile)
              (make-world otetra1 '()))
(check-expect (clean (list 1 0)
                     (clean (list 1 0) world8 clear-line)
                     drop-pile)
              (make-world otetra1 (o-tetra 0 0)))
(check-expect (clean '(2) (clean '(2) world7 clear-line) drop-pile)
              world7.1)

;; clean-all - World -> World
;; clears full lines in the world (and drops the blocks)
(define (clean-all w)
  (local [(define full-ys (full-line-ys (world-pile w)))
          (define cleared-lines (clean full-ys w clear-line))]
    (clean full-ys cleared-lines drop-pile)))
(check-expect (clean-all world6) (make-world otetra1 '()))
(check-expect (clean-all world7) world7.1)
                
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Physics Functions


;; bset-drop - BSet -> BSet
;; moves the blocks in bset down one grid space
(define (bset-drop blocks)
  (map (lambda (b) (make-block (block-x b) (sub1 (block-y b)) (block-color b)))
       blocks))
(check-expect (bset-drop (tetra-blocks otetra1)) (tetra-blocks otetra2))
(check-expect (bset-drop (j-tetra 4 12)) (j-tetra 4 11))

;; tetra-drop - Tetra -> Tetra
;; moves a tetra down one grid space
(define (tetra-drop tetra)
  (make-tetra (make-posn (posn-x (tetra-center tetra))
                         (sub1 (posn-y (tetra-center tetra))))
              (bset-drop (tetra-blocks tetra))))
(check-expect (tetra-drop otetra1) otetra2)
(check-expect (tetra-drop (make-tetra (make-posn 3 14) (j-tetra 2 14)))
              (make-tetra (make-posn 3 13) (j-tetra 2 13)))

;; bset-shift - BSet [Number -> Number] -> BSet
;; Moves the blocks in bset one grid space to the right or left
;; Function takes in add1 for right, sub1 for left
(define (bset-shift blocks func)
  (map (lambda (b) (make-block (func (block-x b)) (block-y b) (block-color b)))
       blocks))
(check-expect (bset-shift (o-tetra 5 15) add1) (o-tetra 6 15))
(check-expect (bset-shift (o-tetra 5 15) sub1) (o-tetra 4 15))

;; tetra-shift - Tetra [Number -> Number] -> Tetra
;; Function - either add1 or sub1
;; Shifts tetra one grid unit
(define (tetra-shift tetra func)
  (make-tetra (make-posn (func (posn-x (tetra-center tetra)))
                         (posn-y (tetra-center tetra)))
              (bset-shift (tetra-blocks tetra) func)))
(check-expect (tetra-shift otetra1 sub1)
              (make-tetra (make-posn 5 16) (bset-shift (o-tetra 5 15) sub1)))
(check-expect (tetra-shift otetra1 add1)
              (make-tetra (make-posn 7 16) (bset-shift (o-tetra 5 15) add1)))

;; block-rotate-ccw : Posn Block -> Block
;; Rotate the block 90 counterclockwise around the posn
(define (block-rotate-ccw p b)
  (make-block (+ (posn-x p)
                 (- (posn-y p) (block-y b)))
              (+ (posn-y p)
                 (- (block-x b) (posn-x p)))
              (block-color b)))
(check-expect (block-rotate-ccw (make-posn 3 3) block1)
              (make-block 2 3 'red))
(check-expect (block-rotate-ccw (make-posn 3 1) block1)
              (make-block 0 1 'red))

;; bset-rotate-ccw : Posn BSet -> BSet
;; Rotates blocks in BSet 90 counterclockwise around the posn
(define (bset-rotate-ccw p b)
  (map (lambda (block) (block-rotate-ccw p block)) b))
(check-expect (bset-rotate-ccw (make-posn 6 15) (o-tetra 5 14))
              (list (make-block 7 14 'green)
                    (make-block 7 15 'green)
                    (make-block 6 14 'green)
                    (make-block 6 15 'green)))           

;; tetra-rotate-ccw : Tetra -> Tetra
;; Rotates the tetra 90 counterclockwise
;; (O-Tetras rotate because the tetras rotate about a block, not a point)
(define (tetra-rotate-ccw tetra)
  (make-tetra (tetra-center tetra) (bset-rotate-ccw
                                    (tetra-center tetra)
                                    (tetra-blocks tetra))))
(check-expect (tetra-rotate-ccw otetra2)
              (make-tetra (make-posn 6 15) (list (make-block 7 14 'green)
                                                 (make-block 7 15 'green)
                                                 (make-block 6 14 'green)
                                                 (make-block 6 15 'green))))

;; block-rotate-cw : Posn Block -> Block
;; Rotate the block 90 clockwise around the posn
(define (block-rotate-cw p b)
  (block-rotate-ccw p (block-rotate-ccw p (block-rotate-ccw p b))))
(check-expect (block-rotate-cw (make-posn 3 3) block1)
              (make-block 4 3 'red))
(check-expect (block-rotate-cw (make-posn 3 1) block1)
              (make-block 6 1 'red))
(check-expect (block-rotate-cw (make-posn 3 5) block1)
              (make-block 2 5 'red))
(check-expect (block-rotate-cw (make-posn 2 4) block1)
              (make-block 2 3 'red))

;; bset-rotate-cw : Posn BSet -> BSet
;; Rotates blocks in BSet 90 clockwise around the posn
(define (bset-rotate-cw p b)
  (map (lambda (block) (block-rotate-cw p block)) b))
(check-expect (bset-rotate-cw (make-posn 6 15) (o-tetra 5 14))
              (list (make-block 5 16 'green)
                    (make-block 5 15 'green)
                    (make-block 6 16 'green)
                    (make-block 6 15 'green)))

;; tetra-rotate-cw : Tetra -> Tetra
;; Rotates the tetra 90 clockwise
(define (tetra-rotate-cw tetra)
  (make-tetra (tetra-center tetra) (bset-rotate-cw
                                    (tetra-center tetra)
                                    (tetra-blocks tetra))))
(check-expect (tetra-rotate-cw otetra2)
              (make-tetra (make-posn 6 15)  (list (make-block 5 16 'green)
                                                  (make-block 5 15 'green)
                                                  (make-block 6 16 'green)
                                                  (make-block 6 15 'green))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Image-Painting Functions


;; place-image/grid - Image Number Number Scene
;; Places image according to grid units
(define (place-image/grid img x y scene)
  (place-image img
               (* GRID-SIZE (+ 1/2 x))
               (* GRID-SIZE (- WORLD-HEIGHT (+ 1/2 y)))
               scene))
(check-expect(place-image/grid (tetrablock block1) 3 4 BACKGROUND)
             (place-image (tetrablock block1)
                          (* GRID-SIZE 7/2)
                          (* GRID-SIZE (- WORLD-HEIGHT 9/2))
                          BACKGROUND))

;; tetrablock - Block -> Image 
;; Makes image of block
(define (tetrablock block)
  (overlay (square GRID-SIZE 'outline 'black)
           (square GRID-SIZE 'solid (block-color block))))
(check-expect (tetrablock block1) (overlay (square GRID-SIZE 'outline 'black)
                                           (square GRID-SIZE 'solid 'red)))

;; bset->image - Bset Scene -> Image
;; Takes a BSet and creates an image of the blocks
(define (bset->image bset scene)
  (cond [(empty? bset) scene]
        [else (place-image/grid (tetrablock (first bset))
                                (block-x (first bset))
                                (block-y (first bset))
                                (bset->image (rest bset) scene))]))
(check-expect (bset->image bset1 BACKGROUND)
              (place-image/grid (tetrablock block1)
                                3 4
                                (place-image/grid (tetrablock block2)
                                                  5 1
                                                  BACKGROUND)))

;; tetra->image - Tetra Scene -> Image
;; Takes a Tetra and creates an image of the Tetra
(define (tetra->image tetra scene)
  (bset->image (tetra-blocks tetra) scene))
(check-expect (tetra->image blocktetra BACKGROUND)
              (place-image/grid (tetrablock block1) 3 4 BACKGROUND))

;; world->image - World -> Image
;; Takes a World and creates an image of the World
(define (world->image w)
  (place-image/grid (text "Score:" 20 'black) 1 (sub1 WORLD-HEIGHT)
                    (place-image/grid (text (number->string (length (world-pile w))) 20 'black)
                                      1 (- WORLD-HEIGHT 2)
                                      (tetra->image (world-tetra w)
                                                    (bset->image (world-pile w) BACKGROUND)))))
(check-expect (world->image world2)
              (place-image/grid (text "Score:" 20 'black)
                                1 19
                                (place-image/grid (text "1" 20 'black)
                                                  1 18
                                                  (place-image/grid (tetrablock block1)
                                                                    3 4
                                                                    (place-image/grid (tetrablock block2)
                                                                                      5 1
                                                                                      BACKGROUND)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; World Functions

; [Tetra -> Tetra] World-> World
; Applies the Tetra function on the world
(define (droprotate func w)
            (make-world (func (world-tetra w)) (world-pile w)))

;; key-move - World KeyEvent -> World
;; Takes a KeyEvent and a world, and moves the Tetra (if possible)
;; (Checks in big-bang)
(define (key-move w key)
  (local [(define (shifthelper func)
            (make-world (tetra-shift (world-tetra w) func) (world-pile w)))]
  (cond [(and (key=? key "left")
              (not (will-collide? (shifthelper sub1))))
         (make-world (tetra-shift (world-tetra w) sub1)
                     (world-pile w))]
        [(and (key=? key "right")
              (not (will-collide? (shifthelper add1))))
         (make-world (tetra-shift (world-tetra w) add1)
                     (world-pile w))]
        [(and (key=? key "down")
              (not (will-collide? (droprotate tetra-drop w))))
         (next-world w)]
        [(and (key=? key "s")
              (not (will-collide? (droprotate tetra-rotate-cw w))))
         (make-world (tetra-rotate-cw (world-tetra w))
                     (world-pile w))]
        [(and (key=? key "a")
              (not (will-collide? (droprotate tetra-rotate-ccw w))))
         (make-world (tetra-rotate-ccw (world-tetra w))
                     (world-pile w))] 
        [else w])))

;; next-world - World -> World
;; returns next world
(define (next-world w)
  (cond [(top? w) (make-world (world-tetra (new-tetra w)) empty)]
        [(bottom? w) (new-tetra w)]
        [(will-collide? (droprotate tetra-drop w)) (new-tetra w)]
        [else (droprotate tetra-drop w)]))
(check-expect (and (empty? (world-pile(next-world topworld)))
                   (equal? (make-posn 5 21)
                           (tetra-center (world-tetra (new-tetra topworld)))))
              true)              
;(check-expect
;(check-expect
(check-expect (next-world world3) (make-world (tetra-drop (world-tetra world3)) '()))

(big-bang (make-world (world-tetra(new-tetra world3)) empty)
          (on-tick next-world 0.3)
          (on-key key-move)
          (to-draw world->image))

