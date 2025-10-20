(ns bt-bag
  (:require [tree]))

(defrecord Bag [tree])

(defn bag-empty []
  (->Bag nil))

(defn bag-insert [bag item]
  (->Bag (tree/insert (:tree bag) item)))

(defn bag-remove [bag item]
  (->Bag (tree/remove-item (:tree bag) item)))

(defn bag-find [bag item]
  (tree/find-item (:tree bag) item))

(defn bag-size [bag]
  (tree/tree-size (:tree bag)))

(defn bag-count [bag]
  (tree/tree-count (:tree bag)))

(defn bag-empty? [bag]
  (tree/tree-empty? (:tree bag)))

(defn bag-seq [bag]
  (tree/inorder (:tree bag)))

(defn bag-equal? [b1 b2]
  (= (bag-seq b1) (bag-seq b2)))

(defn bag-map [f bag]
  (->Bag (tree/from-seq (map f (tree/inorder (:tree bag))))))

(defn bag-filter [pred bag]
  (->Bag (tree/from-seq (filter pred (tree/inorder (:tree bag))))))

(defn bag-foldr [f acc bag]
  (tree/foldr f acc (:tree bag)))

(defn bag-foldl [f acc bag]
  (tree/foldl f acc (:tree bag)))

(defn bag-concat [b1 b2]
  (reduce bag-insert b1 (bag-seq b2)))