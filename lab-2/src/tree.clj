(ns tree)

(defrecord Node [value cnt left right])

(defn insert [node item]
  (cond
    (nil? node) (->Node item 1 nil nil)

    (< item (:value node))
    (->Node (:value node) (:cnt node)
            (insert (:left node) item)
            (:right node))

    (> item (:value node))
    (->Node (:value node) (:cnt node)
            (:left node) (insert (:right node) item))

    :else
    (update node :cnt inc)))

(defn from-seq [coll]
  (reduce insert nil coll))

;; вспомогательная функция для объединения левого и правого поддеревьев
(defn merge-children [left right]
  (if (nil? left)
    right
    (->Node (:value left) (:cnt left)
            (:left left)
            (merge-children (:right left) right))))

(defn remove-item [node item]
  (cond
    (nil? node) nil

    (< item (:value node))
    (->Node (:value node) (:cnt node)
            (remove-item (:left node) item)
            (:right node))

    (> item (:value node))
    (->Node (:value node) (:cnt node)
            (:left node)
            (remove-item (:right node) item))
    :else
    (if (> (:cnt node) 1)
      (update-in node [:cnt] dec)
      (merge-children (:left node) (:right node)))))

(defn find-item [node item]
  (cond
    (nil? node) nil
    (< item (:value node)) (find-item (:left node) item)
    (> item (:value node)) (find-item (:right node) item)
    :else node))

(defn min-node [node]
  (if (nil? (:left node))
    node
    (min-node (:left node))))

(defn inorder [node]
  (when-not (nil? node)
    (concat (inorder (:left node))
            (repeat (:cnt node) (:value node))
            (inorder (:right node)))))

(defn tree-size [node]
  (if (nil? node) 0
      (+ 1 (tree-size (:left node)) (tree-size (:right node)))))

(defn tree-count [node]
  (if (nil? node) 0
      (+ (:cnt node) (tree-count (:left node)) (tree-count (:right node)))))

(defn tree-empty? [node]
  (nil? node))

(defn tree-equal? [node1 node2]
  (cond
    (and (nil? node1) (nil? node2)) true
    (or (nil? node1) (nil? node2)) false
    (and (= (tree-count node1) (tree-count node2))
         (= (tree-size node1) (tree-size node2))
         (tree-equal? (:left node1) (:left node2))
         (tree-equal? (:right node1) (:right node2))) true
    :else false))

(defn foldr [f acc node]
  (if (nil? node)
    acc
    (let [acc-right (foldr f acc (:right node))
          acc-self  (reduce (fn [a _] (f (:value node) a))
                            acc-right
                            (range (:cnt node)))
          acc-left  (foldr f acc-self (:left node))]
      acc-left)))

(defn foldl [f acc node]
  (if (nil? node)
    acc
    (let [acc-left  (foldl f acc (:left node))
          acc-self  (reduce (fn [a _] (f (:value node) a))
                            acc-left
                            (range (:cnt node)))
          acc-right (foldl f acc-self (:right node))]
      acc-right)))