;; Copyright (C) 2011, Eduardo Julián. All rights reserved.
;;
;; The use and distribution terms for this software are covered by the 
;; Eclipse Public License 1.0
;; (http://opensource.org/licenses/eclipse-1.0.php) which can be found
;; in the file epl-v10.html at the root of this distribution.
;;
;; By using this software in any fashion, you are agreeing to be bound
;; by the terms of this license.
;;
;; You must not remove this notice, or any other, from this software.

(ns clj-ebay.core
  "This is a small Clojure binding for the ebay APIs for affiliate marketing."
  (:import (java.net URL URLEncoder))
  (:require [clj-http.client :as client]
    (clojure.data [json :as json])))

; Global Vars
(def ^:dynamic #^{:doc "This var holds the application ID for doing REST calls."}
  *app-id* "")

; Utility fns
(defmacro with-app ""
  [app-id & forms]
  `(binding [*app-id* ~app-id] ~@forms))

(defn _encode-url [url] (if (nil? url) nil (URLEncoder/encode url "UTF-8")))

(defn _fetch-url [url] (-> url client/get :body json/read-str))

(defn _append-arg
  ([url argk argv]
   (if (or (vector? argv) (seq? argv))
     (apply str url "&" argk "=" argv)
     (if argv (str url "&" argk "=" argv) url)))
  ([url argk argv & args] (apply _append-arg (_append-arg url argk argv) args))
  ([url] url))

(defn _my-assoc [m k v]
  (cond
    (nil? (get m k)) (assoc m k v)
    (vector? (get m k)) (assoc m k (conj (get m k) v))
    :else (assoc m k [(get m k) v])))

(defn _bool->int [bool] (if bool 1 0))

(defn _extract-vars [pairs] (->> (map second (partition 2 pairs)) (map #(if (list? %) (second %) %))))
