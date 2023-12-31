(ns midi-mpv.app
  (:require [overtone.midi :as midi])
  (:import [jnr.unixsocket UnixSocketAddress UnixSocketChannel]
           [java.nio.channels Channels]
           [java.io PrintWriter]))

(defn device-descriptions
  []
  (mapv :description (midi/midi-sources)))

(defn socket-writer
  [path]
  (let [address (UnixSocketAddress. path)
        channel (UnixSocketChannel/open address)
        out (Channels/newOutputStream channel)]
    (PrintWriter. out true)))

(defn mpv-command!
  [socket-path s]
  (println "mpv-command!" socket-path (pr-str s))
  (try
    (with-open [writer (socket-writer socket-path)]
      (.println writer s))
    (catch java.io.IOException e
      (println "Exception trying to write to socket" socket-path (.getMessage e)))))

(defn go!
  [{:keys [midi-device-name midi-event->action] :as config}]
  (println "Starting midi-mpv listening to" (pr-str midi-device-name))
  (midi/midi-handle-events
   (midi/midi-in midi-device-name)
   (fn [event]
     (println "received midi" (dissoc event :device :msg))
     (when-let [{:keys [socket-path command]} (midi-event->action event)]
       (mpv-command! socket-path command))))
  (println "midi-mpv is running (interrupt with Ctrl-C)"))
