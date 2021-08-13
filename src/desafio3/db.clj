(ns desafio3.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/desafio3")

(defn abre-conexao []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn apaga-banco []
  (d/delete-database db-uri))

(def schema
  [
   ;CLIENTE
   {:db/ident       :cliente/nome
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "nome do cliente"}
   {:db/ident       :cliente/cpf
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "cpf do cliente"}
   {:db/ident       :cliente/email
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "email do cliente"}
   {:db/ident       :cliente/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   ;CARTAO
   {:db/ident       :cartao/numero
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :cartao/cvv
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :cartao/validade
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one}
   {:db/ident       :cartao/limite
    :db/valueType   :db.type/bigdec
    :db/cardinality :db.cardinality/one}
   {:db/ident       :cartao/cliente
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :cartao/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}

   ;COMPRA
   {:db/ident       :compra/data
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one}
   {:db/ident       :compra/valor
    :db/valueType   :db.type/bigdec
    :db/cardinality :db.cardinality/one}
   {:db/ident       :compra/estabelecimento
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :compra/categoria
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :compra/cartao
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :compra/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}
   ])

(defn cria-schema! [conn]
  (d/transact conn schema))

(defn adiciona-clientes! [conn clientes]
  (d/transact conn clientes))

(defn todas-os-clientes [db]
  (d/q '[:find (pull ?cliente [*])
         :where [?cliente :cliente/id]]
       db))

(defn adiciona-cartoes! [conn cartoes]
  (d/transact conn cartoes))

(defn db-adds-de-atribuicao-de-cliente [cartoes cliente]
  (reduce (fn [db-adds cartao] (conj db-adds [:db/add
                                              [:cartao/id (:cartao/id cartao)]
                                              :cartao/cliente
                                              [:cliente/id (:cliente/id cliente)]]))
          []
          cartoes))


(defn atribui-cliente! [conn cartoes cliente]
  (let [a-transacionar (db-adds-de-atribuicao-de-cliente cartoes cliente)]
    (d/transact conn a-transacionar)))



(defn db-adds-de-atribuicao-de-cartao [compras cartao]
  (reduce (fn [db-adds compra] (conj db-adds [:db/add
                                              [:compra/id (:compra/id compra)]
                                              :compra/cartao
                                              [:cartao/id (:cartao/id cartao)]]))
          []
          compras))


(defn atribui-cartao! [conn compras cartao]
  (let [a-transacionar (db-adds-de-atribuicao-de-cartao compras cartao)]
    (d/transact conn a-transacionar)))

;--- compras
(defn adiciona-compras! [conn compras cartao-icaro]
  (d/transact conn compras)
  (atribui-cartao! conn compras cartao-icaro)
  )

(defn todas-as-compras [db]
  (d/q '[:find (pull ?compra [*])
         :where [?compra :compra/id]]
       db))

(defn compras-no-numero-cartao [db, numero-do-cartao]
  (d/q '[:find  (pull ?compra [*])
         :in $ ?numero
         :where [?cartao :cartao/numero ?numero]
                [?compra :compra/cartao ?cartao]
         ]
       db numero-do-cartao))