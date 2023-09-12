(ns midi-mpv.core
  (:gen-class)
  (:require [midi-mpv.app :as app]))

(def usage
  "Usage:
midi-mpv --start config.clj
midi-mpv --list-devices")

(def env-var-name "MIDI_INPUT_DEVICE")

(defn -main
  [& args]
  (case (first args)

    "--list-devices"
    (doseq [dev (sort (app/device-descriptions))]
      (println dev))

    "--start"
    (let [script (second args)
          device-name (System/getenv env-var-name)]
      (assert script "--start requires a mapper script filename.")
      (assert device-name (format "%s environment variable missing!" env-var-name))
      ;; We expect the return value of the script (the final expression) to be a mapper fn.
      (let [mapper (clojure.main/load-script script)]
        (app/go! {:midi-device-name device-name
                  :midi-event->action mapper})))

    (println usage)))
