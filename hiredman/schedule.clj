(ns hiredman.schedule
    (:import (java.util.concurrent ScheduledThreadPoolExecutor TimeUnit)))
 
(def unit {:minutes TimeUnit/MINUTES :seconds TimeUnit/SECONDS})

(def tasks (ref {}))

(def #^{:doc "ScheduledThreadPoolExecutor for scheduling repeated/delayed tasks"}
     task-runner (ScheduledThreadPoolExecutor. (+ 1 (.availableProcessors (Runtime/getRuntime)))))

(defn fixedrate [name task t1 t2 tu]
      (let [ft (.scheduleAtFixedRate task-runner
                                     (long t1)
                                     (long t2)
                                     tu)]
        (dosync
          (alter tasks assoc name ft))))

(defn cancel [name]
      (.cancel (get @tasks name) true)
      (dosync
        (alter tasks dissoc name)))
