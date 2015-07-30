(ns Closer.file_search
    (:require [clojure.java.io :as io]))


(let [source-directory (io/file "c:/target/")
      timeout 1
      end-time (+ (System/currentTimeMillis) (* timeout 1000))
      min-file-size-bytes 50000000000
      file-name-pattern #"^.*java$"
      file-name-criteria (fn [file] (if-not (nil? file) (if (re-matches file-name-pattern (.getName file))  file)))
      file-size-criteria (fn [file]  (if-not (nil? file) (if (> (.getTotalSpace file)  min-file-size-bytes ) file)))
      time-out-criteria (fn [file]  (if (< (System/currentTimeMillis) end-time) file (throw (Exception. "Time out"))))
      apply-criteria (fn [file] (apply (comp time-out-criteria file-size-criteria file-name-criteria ) [file]))
      deep-dive (fn [] (filter apply-criteria (file-seq source-directory)))]

(map #(println (str (.getName %) " " (.getTotalSpace %))) (deep-dive)))
