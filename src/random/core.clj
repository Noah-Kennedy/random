(ns random.core
  (:import (java.util ArrayList LinkedList)))

(def message-queue (ref (LinkedList.)))

(defn send-to-queue [message]
  (dosync
    (.push (deref message-queue) message)))

(defn recieve-from-queue []
  (dosync
    (.pop (deref message-queue))))

(defn start-recieving []
  (.start
    (Thread.
      #(dosync
         (while
           (not-empty
             (deref message-queue))
           (println (recieve-from-queue)))))))

(defn start-sending []
  (.start
    (Thread.
      #(loop [n 0]
         (send-to-queue n)
         (when (< n 100)
           (recur (inc n)))))))

(defn start-both []
  (do
    (start-sending)
    (start-recieving)))