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
    :db/valueType   :db.type/string
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
