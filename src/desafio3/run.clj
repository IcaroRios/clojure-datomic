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

(def cartao-icaro   (model/novo-cartao 5410133996442556, 984, #inst "2022-10-20", 10000M))
(def cartao-nyelson (model/novo-cartao 545926820005900, 737,  #inst "2023-05-05", 10000M))

(pprint @(db/adiciona-cartoes! conn [cartao-icaro, cartao-nyelson]))
(db/atribui-cartao! conn [cartao-icaro] icaro)

(def cartoes (db/todas-os-cartoes (d/db conn)))
(println cartoes)