(ns desafio3.run
  (:use clojure.pprint)
  (:require [datomic.api :as d])
  (:require [desafio3.db :as db])
  (:require [desafio3.model :as model])
  )

(db/apaga-banco)

(def conn (db/abre-conexao))

(db/cria-schema! conn)

(def nyelson (model/novo-cliente "Nyelson Barbosa", "22667862023", "nyelson.barbosa@nubank.com.br"))
(def icaro (model/novo-cliente "Icaro Rios", "32257409000", "icaro.rios@nubank.com.br"))

(pprint @(db/adiciona-clientes! conn [icaro, nyelson]))

(def clientes (db/todas-os-clientes (d/db conn)))
(println clientes)