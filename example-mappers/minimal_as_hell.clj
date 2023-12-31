;; Ultra minimal config file example.
;; Pause/unpause in response to any midi event.
;; We don't even look at the event in this example.

(constantly
  {:command     "cycle pause"
   :socket-path "/tmp/mpv-socket.0"})
