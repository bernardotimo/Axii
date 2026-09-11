-- ---------------------------------------------------------------------
-- 1. COMANDOS PARA EXCLUIR AS TABELAS
-- Na primeira execução as tabelas ainda não existem e o SGBD exibirá o
-- erro ORA-00942 para cada DROP. Basta ignorar e seguir a execução.
-- ---------------------------------------------------------------------
DROP TABLE t_axii_coin          CASCADE CONSTRAINTS;
DROP TABLE t_axii_stablecoin    CASCADE CONSTRAINTS;
DROP TABLE t_axii_crypto_asset  CASCADE CONSTRAINTS;
DROP TABLE t_axii_pix_key       CASCADE CONSTRAINTS;
DROP TABLE t_axii_bank          CASCADE CONSTRAINTS;
DROP TABLE t_axii_user          CASCADE CONSTRAINTS;
DROP TABLE t_axii_identity      CASCADE CONSTRAINTS;
DROP TABLE t_axii_notifications CASCADE CONSTRAINTS;
DROP TABLE t_axii_settings      CASCADE CONSTRAINTS;

-- ---------------------------------------------------------------------
-- 2. COMANDOS PARA CRIAR AS TABELAS
--    E ADICIONAR CONSTRAINTS: PRIMARY KEY, UNIQUE E CHECK
-- ---------------------------------------------------------------------

CREATE TABLE t_axii_settings (
    id_settings  VARCHAR2(36)           NOT NULL,
    modo_escuro  NUMBER(1)    DEFAULT 0 NOT NULL,
    idioma       VARCHAR2(10)           NOT NULL
);

COMMENT ON TABLE t_axii_settings IS
    'Configuracoes de interface e idioma do usuario. Relacao 1:1 com User.';
COMMENT ON COLUMN t_axii_settings.modo_escuro IS
    'Indica se o tema escuro esta ativado (0 = nao, 1 = sim).';
COMMENT ON COLUMN t_axii_settings.idioma IS
    'Codigo do idioma escolhido pelo usuario. Ex.: pt-BR, en-US.';

   ALTER TABLE t_axii_settings
ADD CONSTRAINT pk_axii_settings
   PRIMARY KEY ( id_settings );

   ALTER TABLE t_axii_settings
ADD CONSTRAINT ck_axii_settings_modo_escuro
         CHECK ( modo_escuro IN (0, 1) );


CREATE TABLE t_axii_notifications (
    id_notifications  VARCHAR2(36)           NOT NULL,
    transacao         NUMBER(1)    DEFAULT 0 NOT NULL,
    variacao_preco    NUMBER(1)    DEFAULT 0 NOT NULL,
    marketing         NUMBER(1)    DEFAULT 0 NOT NULL
);

COMMENT ON TABLE t_axii_notifications IS
    'Preferencias de notificacao do usuario. Relacao 1:1 com User.';
COMMENT ON COLUMN t_axii_notifications.transacao IS
    'Recebe notificacao de transacoes realizadas (0 = nao, 1 = sim).';
COMMENT ON COLUMN t_axii_notifications.variacao_preco IS
    'Recebe notificacao de variacao de preco dos ativos (0 = nao, 1 = sim).';
COMMENT ON COLUMN t_axii_notifications.marketing IS
    'Recebe comunicacoes de marketing (0 = nao, 1 = sim).';

   ALTER TABLE t_axii_notifications
ADD CONSTRAINT pk_axii_notifications
   PRIMARY KEY ( id_notifications );

   ALTER TABLE t_axii_notifications
ADD CONSTRAINT ck_axii_notif_transacao
         CHECK ( transacao IN (0, 1) );

   ALTER TABLE t_axii_notifications
ADD CONSTRAINT ck_axii_notif_variacao_preco
         CHECK ( variacao_preco IN (0, 1) );

   ALTER TABLE t_axii_notifications
ADD CONSTRAINT ck_axii_notif_marketing
         CHECK ( marketing IN (0, 1) );


CREATE TABLE t_axii_identity (
    id_identity  VARCHAR2(36)  NOT NULL,
    telefone     VARCHAR2(20)  NOT NULL,
    cpf          VARCHAR2(14)  NOT NULL
);

COMMENT ON TABLE t_axii_identity IS
    'Dados pessoais de identificacao do usuario. Relacao 1:1 com User.';
COMMENT ON COLUMN t_axii_identity.cpf IS
    'CPF do usuario com formatacao: xxx.xxx.xxx-xx.';

   ALTER TABLE t_axii_identity
ADD CONSTRAINT pk_axii_identity
   PRIMARY KEY ( id_identity );

   ALTER TABLE t_axii_identity
ADD CONSTRAINT un_axii_identity_cpf
        UNIQUE ( cpf );

-- ADIÇÃO DA CONSTRAINT CHECK (formato xxx.xxx.xxx-xx)
   ALTER TABLE t_axii_identity
ADD CONSTRAINT ck_axii_identity_cpf
         CHECK ( cpf LIKE '___.___.___-__' );


CREATE TABLE t_axii_user (
    id_user           VARCHAR2(36)                        NOT NULL,
    nome              VARCHAR2(100)                       NOT NULL,
    email             VARCHAR2(150)                       NOT NULL,
    senha             VARCHAR2(255)                       NOT NULL,
    data_criacao      TIMESTAMP     DEFAULT SYSTIMESTAMP  NOT NULL,
    data_atualizacao  TIMESTAMP     DEFAULT SYSTIMESTAMP  NOT NULL,
    id_settings       VARCHAR2(36)                        NOT NULL,
    id_notifications  VARCHAR2(36)                        NOT NULL,
    id_identity       VARCHAR2(36)                        NOT NULL
);

COMMENT ON TABLE t_axii_user IS
    'Usuario da plataforma. Entidade central do modelo.';
COMMENT ON COLUMN t_axii_user.id_user IS
    'Identificador unico do usuario (UUID).';
COMMENT ON COLUMN t_axii_user.senha IS
    'Senha de acesso (recomendado armazenar em hash). Minimo 6 caracteres.';

   ALTER TABLE t_axii_user
ADD CONSTRAINT pk_axii_user
   PRIMARY KEY ( id_user );

   ALTER TABLE t_axii_user
ADD CONSTRAINT un_axii_user_email
        UNIQUE ( email );

   ALTER TABLE t_axii_user
ADD CONSTRAINT un_axii_user_settings
        UNIQUE ( id_settings );

   ALTER TABLE t_axii_user
ADD CONSTRAINT un_axii_user_notifications
        UNIQUE ( id_notifications );

   ALTER TABLE t_axii_user
ADD CONSTRAINT un_axii_user_identity
        UNIQUE ( id_identity );

   ALTER TABLE t_axii_user
ADD CONSTRAINT ck_axii_user_email
         CHECK ( email LIKE '%@%' );

   ALTER TABLE t_axii_user
ADD CONSTRAINT ck_axii_user_senha
         CHECK ( LENGTH(senha) >= 6 );


CREATE TABLE t_axii_bank (
    id_bank       VARCHAR2(36)            NOT NULL,
    nome_banco    VARCHAR2(100)           NOT NULL,
    ativo         NUMBER(1)     DEFAULT 1 NOT NULL,
    numero_conta  VARCHAR2(20)            NOT NULL,
    agencia       NUMBER(6)               NOT NULL,
    id_user       VARCHAR2(36)            NOT NULL
);

COMMENT ON TABLE t_axii_bank IS
    'Contas bancarias vinculadas ao usuario. Relacao N:1 com User.';
COMMENT ON COLUMN t_axii_bank.ativo IS
    'Indica se a conta esta ativa (0 = nao, 1 = sim).';

   ALTER TABLE t_axii_bank
ADD CONSTRAINT pk_axii_bank
   PRIMARY KEY ( id_bank );

   ALTER TABLE t_axii_bank
ADD CONSTRAINT ck_axii_bank_ativo
         CHECK ( ativo IN (0, 1) );

   ALTER TABLE t_axii_bank
ADD CONSTRAINT ck_axii_bank_agencia
         CHECK ( agencia > 0 );


CREATE TABLE t_axii_pix_key (
    id_pix_key  VARCHAR2(36)   NOT NULL,
    chave       VARCHAR2(150)  NOT NULL,
    tipo        VARCHAR2(30)   NOT NULL,
    id_user     VARCHAR2(36)   NOT NULL
);

COMMENT ON TABLE t_axii_pix_key IS
    'Chaves Pix cadastradas pelo usuario. Relacao N:1 com User.';

   ALTER TABLE t_axii_pix_key
ADD CONSTRAINT pk_axii_pix_key
   PRIMARY KEY ( id_pix_key );

   ALTER TABLE t_axii_pix_key
ADD CONSTRAINT un_axii_pix_key_chave
        UNIQUE ( chave );

   ALTER TABLE t_axii_pix_key
ADD CONSTRAINT ck_axii_pix_key_tipo
         CHECK ( tipo IN ('celular', 'e-mail', 'cpf', 'cnpj', 'aleatoria') );


CREATE TABLE t_axii_crypto_asset (
    id_crypto_asset  VARCHAR2(36)                        NOT NULL,
    ativo            VARCHAR2(100)                       NOT NULL,
    quantidade       NUMBER(20, 8)                       NOT NULL,
    data_aquisicao   TIMESTAMP     DEFAULT SYSTIMESTAMP  NOT NULL,
    origem           VARCHAR2(50)                        NOT NULL,
    tipo_ativo       VARCHAR2(20)                        NOT NULL,
    id_user          VARCHAR2(36)                        NOT NULL
);

COMMENT ON TABLE t_axii_crypto_asset IS
    'Ativos de criptomoeda do usuario. Supertipo da heranca (Coin e Stablecoin).';
COMMENT ON COLUMN t_axii_crypto_asset.tipo_ativo IS
    'Discriminador da heranca: COIN ou STABLECOIN.';

   ALTER TABLE t_axii_crypto_asset
ADD CONSTRAINT pk_axii_crypto_asset
   PRIMARY KEY ( id_crypto_asset );

   ALTER TABLE t_axii_crypto_asset
ADD CONSTRAINT ck_axii_crypto_quantidade
         CHECK ( quantidade >= 0 );

   ALTER TABLE t_axii_crypto_asset
ADD CONSTRAINT ck_axii_crypto_tipo_ativo
         CHECK ( tipo_ativo IN ('COIN', 'STABLECOIN') );


CREATE TABLE t_axii_coin (
    id_coin        VARCHAR2(36)   NOT NULL,
    simbolo        VARCHAR2(10)   NOT NULL,
    blockchain     VARCHAR2(50)   NOT NULL,
    preco_mercado  NUMBER(20, 2)  NOT NULL
);

COMMENT ON TABLE t_axii_coin IS
    'Subtipo de CryptoAsset que representa moedas volateis (ex.: Bitcoin, Ethereum).';
COMMENT ON COLUMN t_axii_coin.preco_mercado IS
    'Preco unitario de mercado da moeda (em BRL).';

   ALTER TABLE t_axii_coin
ADD CONSTRAINT pk_axii_coin
   PRIMARY KEY ( id_coin );

   ALTER TABLE t_axii_coin
ADD CONSTRAINT ck_axii_coin_preco_mercado
         CHECK ( preco_mercado > 0 );


CREATE TABLE t_axii_stablecoin (
    id_stablecoin     VARCHAR2(36)   NOT NULL,
    moeda_referencia  VARCHAR2(10)   NOT NULL,
    taxa_conversao    NUMBER(20, 4)  NOT NULL
);

COMMENT ON TABLE t_axii_stablecoin IS
    'Subtipo de CryptoAsset que representa moedas estaveis lastreadas em moeda fiduciaria.';
COMMENT ON COLUMN t_axii_stablecoin.taxa_conversao IS
    'Taxa de conversao para a moeda de referencia (em BRL).';

   ALTER TABLE t_axii_stablecoin
ADD CONSTRAINT pk_axii_stablecoin
   PRIMARY KEY ( id_stablecoin );

   ALTER TABLE t_axii_stablecoin
ADD CONSTRAINT ck_axii_stable_taxa_conversao
         CHECK ( taxa_conversao > 0 );


-- ---------------------------------------------------------------------
-- 3. ADIÇÃO DAS CONSTRAINTS CHAVE ESTRANGEIRA - RELACIONAMENTOS
-- ---------------------------------------------------------------------

-- USER E SETTINGS (1:1)
   ALTER TABLE t_axii_user
ADD CONSTRAINT fk_axii_user_settings
   FOREIGN KEY ( id_settings )
    REFERENCES t_axii_settings ( id_settings );

-- USER E NOTIFICATIONS (1:1)
   ALTER TABLE t_axii_user
ADD CONSTRAINT fk_axii_user_notifications
   FOREIGN KEY ( id_notifications )
    REFERENCES t_axii_notifications ( id_notifications );

-- USER E IDENTITY (1:1)
   ALTER TABLE t_axii_user
ADD CONSTRAINT fk_axii_user_identity
   FOREIGN KEY ( id_identity )
    REFERENCES t_axii_identity ( id_identity );

-- BANK E USER (N:1)
   ALTER TABLE t_axii_bank
ADD CONSTRAINT fk_axii_bank_user
   FOREIGN KEY ( id_user )
    REFERENCES t_axii_user ( id_user )
     ON DELETE CASCADE;

-- PIX_KEY E USER (N:1)
   ALTER TABLE t_axii_pix_key
ADD CONSTRAINT fk_axii_pix_key_user
   FOREIGN KEY ( id_user )
    REFERENCES t_axii_user ( id_user )
     ON DELETE CASCADE;

-- CRYPTO_ASSET E USER (N:1)
   ALTER TABLE t_axii_crypto_asset
ADD CONSTRAINT fk_axii_crypto_asset_user
   FOREIGN KEY ( id_user )
    REFERENCES t_axii_user ( id_user )
     ON DELETE CASCADE;

-- COIN E CRYPTO_ASSET (herança 1:1)
   ALTER TABLE t_axii_coin
ADD CONSTRAINT fk_axii_coin_crypto_asset
   FOREIGN KEY ( id_coin )
    REFERENCES t_axii_crypto_asset ( id_crypto_asset )
     ON DELETE CASCADE;

-- RELACIONAMENTO ENTRE STABLECOIN E CRYPTO_ASSET (herança 1:1)
   ALTER TABLE t_axii_stablecoin
ADD CONSTRAINT fk_axii_stable_crypto_asset
   FOREIGN KEY ( id_stablecoin )
    REFERENCES t_axii_crypto_asset ( id_crypto_asset )
     ON DELETE CASCADE;


-- ---------------------------------------------------------------------
-- 4. ÍNDICES NAS COLUNAS DE CHAVE ESTRANGEIRA
-- ---------------------------------------------------------------------

CREATE INDEX ix_axii_bank_user         ON t_axii_bank         ( id_user );
CREATE INDEX ix_axii_pix_key_user      ON t_axii_pix_key      ( id_user );
CREATE INDEX ix_axii_crypto_asset_user ON t_axii_crypto_asset ( id_user );