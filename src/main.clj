(ns main
  (:require [clojure.core.match :as match]
            [clojure.string :as str]
            [hiccup2.core :refer [html]]
            [selmer.parser :refer [render-file]]))

(defn new-content []
  (str (html [:div
              [:h4 "New Content"]
              [:button {:hx-get "/initial-content"
                        :hx-swap "innerHTML transition:true"
                        :hx-target "closest div"}
               "Restore It!"]])))

(defn initial-content []
  (str (html [:div
              [:h4 "Initial Content"]
              [:button {:hx-get "/new-content"
                        :hx-swap "innerHTML transition:true"
                        :hx-target "closest div"}
               "Swap It!"]])))

(defn router [req]
  (let [paths (some-> (:uri req) (str/split #"/") rest vec)
        verb (:request-method req)]
    (match/match [verb paths]
      [:get []] {:body (render-file "animations.html" {})}
      [:get ["new-content"]] {:body (new-content)}
      [:get ["initial-content"]] {:body (initial-content)}
      :else {:status 404 :body "not found"})))

(comment
  (router {:request-method :get :uri "/new-content"}))