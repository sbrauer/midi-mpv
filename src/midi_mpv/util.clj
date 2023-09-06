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

(defn calculate-14-bit-value
  "Calculates the the 14 bit value given two integers
  representing the high and low parts of a 14 bit value.
  Returns an intege in the range 0 to 16383."
  [lower higher]
  (bit-or (bit-and lower 0x7f)
          (bit-shift-left (bit-and higher 0x7f)
                          7)))

(defn interpret-pitch-bend-data
  "Given data1 and data2 values from a midi pitchbend event, return a value in the range of -1.0 to 1.0, where 0.0 represents the middle value (no bend)."
  [data1 data2]
  (let [middle 8192
        v14 (calculate-14-bit-value data1 data2)]
    (/ (- v14 middle)
       (float (dec middle)))))
