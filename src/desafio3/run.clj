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
(def sem-venda (model/novo-cliente "Icaro Rios", "32257409000", "icaro.rios@nubank.com.br"))

(db/adiciona-clientes! conn [icaro, nyelson, sem-venda])

(db/todas-os-clientes (d/db conn))

(def cartao-icaro (model/novo-cartao 5410133996442556, 984, #inst "2022-10-20", 10000M))
(def cartao-nyelson (model/novo-cartao 545926820005900, 737, #inst "2023-05-05", 10000M))

(db/adiciona-cartoes! conn [cartao-icaro, cartao-nyelson])
(db/atribui-cliente! conn [cartao-icaro] icaro)

(def compras [
              (model/nova-compra #inst "2021-10-20", 1000M, "Adidas", "Vestuário")
              (model/nova-compra #inst "2021-10-21", 250M, "Adidas", "Vestuário")
              (model/nova-compra #inst "2021-03-13", 40M, "Burguer King", "Restaurante")
              (model/nova-compra #inst "2021-05-29", 300M, "Nike", "Vestuário")
              (model/nova-compra #inst "2021-12-30", 50M, "Cinemark", "Cinema")
              (model/nova-compra #inst "2021-12-27", 5M, "Padaria", "Alimentos")
              ])

(def compras-nyelson [(model/nova-compra #inst "2021-10-25", 100000M, "Hollister", "Vestuário")
                      (model/nova-compra #inst "2021-12-25", 70M, "Boliche", "Aleatório")
                      ])

(db/adiciona-compras! conn compras cartao-icaro)

(db/adiciona-compras! conn compras-nyelson cartao-nyelson)

(println "Compras de ícaro")
(pprint (db/compras-no-numero-cartao (d/db conn) (:cartao/numero cartao-icaro)))

(println "Compras de Nyelson")
(pprint (db/compras-no-numero-cartao (d/db conn) (:cartao/numero cartao-nyelson)))

