(ns midi-mpv.util)

(defn octave
  [idx]
  (* idx 12))

(def C   0)
(def C#  1)
(def D   2)
(def D#  3)
(def E   4)
(def F   5)
(def F#  6)
(def G   7)
(def G#  8)
(def A   9)
(def A# 10)
(def B  11)

;; Use this to interpret the data1 and data2 from a :pitch-bend command event.
(defn calculate-14-bit-value
  "Calculates the the 14 bit value given two integers
representing the high and low parts of a 14 bit value."
  [lower higher]
  (bit-or (bit-and lower 0x7f)
   (bit-shift-left (bit-and higher 0x7f)
     7)))
