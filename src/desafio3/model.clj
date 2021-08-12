(ns desafio3.model)

(defn uuid [] (java.util.UUID/randomUUID))

(defn novo-cliente
  ([nome cpf email]
   (novo-cliente (uuid) nome cpf email))
  ([uuid nome cpf email]
   {
    :cliente/id    uuid
    :cliente/nome  nome
    :cliente/cpf   cpf
    :cliente/email email
    }))

(defn novo-cartao
  ([numero cvv validade limite]
   {:cartao/id       (uuid)
    :cartao/:numero  numero
    :cartao/cvv      cvv
    :cartao/validade validade
    :cartao/limite   limite
    }))

(defn nova-compra
  ([data valor estabelecimento categoria]
   {:compra/id              (uuid)
    :compra/data            data
    :compra/valor           valor
    :compra/estabelecimento estabelecimento
    :compra/categoria       categoria
    }))
