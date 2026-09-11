-- ---------------------------------------------------------------------
-- 1. INSERT
-- ---------------------------------------------------------------------

-- SETTINGS
INSERT INTO t_axii_settings (id_settings, modo_escuro, idioma)
VALUES ('b0000000-0000-4000-8000-000000000001', 1, 'pt-BR');

INSERT INTO t_axii_settings (id_settings, modo_escuro, idioma)
VALUES ('b0000000-0000-4000-8000-000000000002', 0, 'en-US');

INSERT INTO t_axii_settings (id_settings, modo_escuro, idioma)
VALUES ('b0000000-0000-4000-8000-000000000003', 1, 'pt-BR');

INSERT INTO t_axii_settings (id_settings, modo_escuro, idioma)
VALUES ('b0000000-0000-4000-8000-000000000004', 0, 'pt-BR');

-- modo_escuro omitido: assume o valor DEFAULT 0
INSERT INTO t_axii_settings (id_settings, idioma)
VALUES ('b0000000-0000-4000-8000-000000000005', 'es-ES');

-- NOTIFICATIONS
INSERT INTO t_axii_notifications (id_notifications, transacao, variacao_preco, marketing)
VALUES ('c0000000-0000-4000-8000-000000000001', 1, 1, 0);

INSERT INTO t_axii_notifications (id_notifications, transacao, variacao_preco, marketing)
VALUES ('c0000000-0000-4000-8000-000000000002', 1, 0, 0);

INSERT INTO t_axii_notifications (id_notifications, transacao, variacao_preco, marketing)
VALUES ('c0000000-0000-4000-8000-000000000003', 1, 1, 1);

INSERT INTO t_axii_notifications (id_notifications, transacao, variacao_preco, marketing)
VALUES ('c0000000-0000-4000-8000-000000000004', 0, 1, 0);

-- transacao, variacao_preco e marketing omitidos: assumem o valor DEFAULT 0
INSERT INTO t_axii_notifications (id_notifications)
VALUES ('c0000000-0000-4000-8000-000000000005');

-- IDENTITY
INSERT INTO t_axii_identity (id_identity, telefone, cpf)
VALUES ('d0000000-0000-4000-8000-000000000001', '(11) 98765-4321', '123.456.789-01');

INSERT INTO t_axii_identity (id_identity, telefone, cpf)
VALUES ('d0000000-0000-4000-8000-000000000002', '(21) 99876-5432', '234.567.890-12');

INSERT INTO t_axii_identity (id_identity, telefone, cpf)
VALUES ('d0000000-0000-4000-8000-000000000003', '(31) 98888-7777', '345.678.901-23');

INSERT INTO t_axii_identity (id_identity, telefone, cpf)
VALUES ('d0000000-0000-4000-8000-000000000004', '(41) 97777-6666', '456.789.012-34');

INSERT INTO t_axii_identity (id_identity, telefone, cpf)
VALUES ('d0000000-0000-4000-8000-000000000005', '(51) 96666-5555', '567.890.123-45');

-- USER
INSERT INTO t_axii_user (id_user, nome, email, senha, data_criacao, data_atualizacao,
                         id_settings, id_notifications, id_identity)
VALUES ('a0000000-0000-4000-8000-000000000001', 'Ana Souza', 'ana.souza@email.com', 'Ana@2025',
        TO_TIMESTAMP('2025-01-15 09:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2025-01-15 09:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        'b0000000-0000-4000-8000-000000000001',
        'c0000000-0000-4000-8000-000000000001',
        'd0000000-0000-4000-8000-000000000001');

INSERT INTO t_axii_user (id_user, nome, email, senha, data_criacao, data_atualizacao,
                         id_settings, id_notifications, id_identity)
VALUES ('a0000000-0000-4000-8000-000000000002', 'Bruno Lima', 'bruno.lima@email.com', 'Bruno#123',
        TO_TIMESTAMP('2025-02-03 14:10:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2025-02-03 14:10:00', 'YYYY-MM-DD HH24:MI:SS'),
        'b0000000-0000-4000-8000-000000000002',
        'c0000000-0000-4000-8000-000000000002',
        'd0000000-0000-4000-8000-000000000002');

INSERT INTO t_axii_user (id_user, nome, email, senha, data_criacao, data_atualizacao,
                         id_settings, id_notifications, id_identity)
VALUES ('a0000000-0000-4000-8000-000000000003', 'Carla Mendes', 'carla.mendes@email.com', 'Carla$456',
        TO_TIMESTAMP('2025-03-21 18:45:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2025-03-21 18:45:00', 'YYYY-MM-DD HH24:MI:SS'),
        'b0000000-0000-4000-8000-000000000003',
        'c0000000-0000-4000-8000-000000000003',
        'd0000000-0000-4000-8000-000000000003');

INSERT INTO t_axii_user (id_user, nome, email, senha, data_criacao, data_atualizacao,
                         id_settings, id_notifications, id_identity)
VALUES ('a0000000-0000-4000-8000-000000000004', 'Diego Rocha', 'diego.rocha@email.com', 'Diego%789',
        TO_TIMESTAMP('2025-05-08 11:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2025-05-08 11:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        'b0000000-0000-4000-8000-000000000004',
        'c0000000-0000-4000-8000-000000000004',
        'd0000000-0000-4000-8000-000000000004');

-- data_criacao e data_atualizacao omitidas: assumem o valor DEFAULT SYSTIMESTAMP
INSERT INTO t_axii_user (id_user, nome, email, senha, id_settings, id_notifications, id_identity)
VALUES ('a0000000-0000-4000-8000-000000000005', 'Eduarda Alves', 'eduarda.alves@email.com', 'Duda&2026',
        'b0000000-0000-4000-8000-000000000005',
        'c0000000-0000-4000-8000-000000000005',
        'd0000000-0000-4000-8000-000000000005');

-- BANK
INSERT INTO t_axii_bank (id_bank, nome_banco, ativo, numero_conta, agencia, id_user)
VALUES ('e0000000-0000-4000-8000-000000000001', 'Inter', 1, '12345-6', 1,
        'a0000000-0000-4000-8000-000000000001');

INSERT INTO t_axii_bank (id_bank, nome_banco, ativo, numero_conta, agencia, id_user)
VALUES ('e0000000-0000-4000-8000-000000000002', 'Nubank', 1, '87654-3', 1,
        'a0000000-0000-4000-8000-000000000001');

INSERT INTO t_axii_bank (id_bank, nome_banco, ativo, numero_conta, agencia, id_user)
VALUES ('e0000000-0000-4000-8000-000000000003', 'Santander', 1, '98765-4', 3456,
        'a0000000-0000-4000-8000-000000000002');

INSERT INTO t_axii_bank (id_bank, nome_banco, ativo, numero_conta, agencia, id_user)
VALUES ('e0000000-0000-4000-8000-000000000004', 'Itau', 1, '54321-0', 1500,
        'a0000000-0000-4000-8000-000000000003');

INSERT INTO t_axii_bank (id_bank, nome_banco, ativo, numero_conta, agencia, id_user)
VALUES ('e0000000-0000-4000-8000-000000000005', 'Bradesco', 1, '11223-3', 2020,
        'a0000000-0000-4000-8000-000000000004');

-- ativo omitido: assume o valor DEFAULT 1
INSERT INTO t_axii_bank (id_bank, nome_banco, numero_conta, agencia, id_user)
VALUES ('e0000000-0000-4000-8000-000000000006', 'Banco do Brasil', '44556-7', 1234,
        'a0000000-0000-4000-8000-000000000005');

-- PIX_KEY
INSERT INTO t_axii_pix_key (id_pix_key, chave, tipo, id_user)
VALUES ('f0000000-0000-4000-8000-000000000001', 'ana.souza@email.com', 'e-mail',
        'a0000000-0000-4000-8000-000000000001');

INSERT INTO t_axii_pix_key (id_pix_key, chave, tipo, id_user)
VALUES ('f0000000-0000-4000-8000-000000000002', '+5521998765432', 'celular',
        'a0000000-0000-4000-8000-000000000002');

INSERT INTO t_axii_pix_key (id_pix_key, chave, tipo, id_user)
VALUES ('f0000000-0000-4000-8000-000000000003', '7d9f1c2e-3b4a-4f6d-9e8c-1a2b3c4d5e6f', 'aleatoria',
        'a0000000-0000-4000-8000-000000000003');

INSERT INTO t_axii_pix_key (id_pix_key, chave, tipo, id_user)
VALUES ('f0000000-0000-4000-8000-000000000004', '12.345.678/0001-90', 'cnpj',
        'a0000000-0000-4000-8000-000000000004');

INSERT INTO t_axii_pix_key (id_pix_key, chave, tipo, id_user)
VALUES ('f0000000-0000-4000-8000-000000000005', '567.890.123-45', 'cpf',
        'a0000000-0000-4000-8000-000000000005');

-- CRYPTO_ASSET (supertipo)
INSERT INTO t_axii_crypto_asset (id_crypto_asset, ativo, quantidade, data_aquisicao, origem, tipo_ativo, id_user)
VALUES ('10000000-0000-4000-8000-000000000001', 'Bitcoin', 0.05234100,
        TO_TIMESTAMP('2025-01-20 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Binance', 'COIN',
        'a0000000-0000-4000-8000-000000000001');

INSERT INTO t_axii_crypto_asset (id_crypto_asset, ativo, quantidade, data_aquisicao, origem, tipo_ativo, id_user)
VALUES ('10000000-0000-4000-8000-000000000002', 'Tether', 1500.00000000,
        TO_TIMESTAMP('2025-02-01 16:20:00', 'YYYY-MM-DD HH24:MI:SS'), 'Binance', 'STABLECOIN',
        'a0000000-0000-4000-8000-000000000001');

INSERT INTO t_axii_crypto_asset (id_crypto_asset, ativo, quantidade, data_aquisicao, origem, tipo_ativo, id_user)
VALUES ('10000000-0000-4000-8000-000000000003', 'Ethereum', 1.25000000,
        TO_TIMESTAMP('2025-02-10 08:15:00', 'YYYY-MM-DD HH24:MI:SS'), 'Coinbase', 'COIN',
        'a0000000-0000-4000-8000-000000000002');

INSERT INTO t_axii_crypto_asset (id_crypto_asset, ativo, quantidade, data_aquisicao, origem, tipo_ativo, id_user)
VALUES ('10000000-0000-4000-8000-000000000004', 'Solana', 30.50000000,
        TO_TIMESTAMP('2025-04-02 21:40:00', 'YYYY-MM-DD HH24:MI:SS'), 'Manual', 'COIN',
        'a0000000-0000-4000-8000-000000000003');

INSERT INTO t_axii_crypto_asset (id_crypto_asset, ativo, quantidade, data_aquisicao, origem, tipo_ativo, id_user)
VALUES ('10000000-0000-4000-8000-000000000005', 'USD Coin', 800.00000000,
        TO_TIMESTAMP('2025-04-15 13:05:00', 'YYYY-MM-DD HH24:MI:SS'), 'Coinbase', 'STABLECOIN',
        'a0000000-0000-4000-8000-000000000003');

INSERT INTO t_axii_crypto_asset (id_crypto_asset, ativo, quantidade, data_aquisicao, origem, tipo_ativo, id_user)
VALUES ('10000000-0000-4000-8000-000000000006', 'Bitcoin', 0.01000000,
        TO_TIMESTAMP('2025-05-10 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Mercado Bitcoin', 'COIN',
        'a0000000-0000-4000-8000-000000000004');

-- data_aquisicao omitida: assume o valor DEFAULT SYSTIMESTAMP
INSERT INTO t_axii_crypto_asset (id_crypto_asset, ativo, quantidade, origem, tipo_ativo, id_user)
VALUES ('10000000-0000-4000-8000-000000000007', 'Tether', 200.00000000, 'Binance', 'STABLECOIN',
        'a0000000-0000-4000-8000-000000000005');

-- COIN (subtipo) - usa o mesmo id do CRYPTO_ASSET correspondente
INSERT INTO t_axii_coin (id_coin, simbolo, blockchain, preco_mercado)
VALUES ('10000000-0000-4000-8000-000000000001', 'BTC', 'Bitcoin', 350000.00);

INSERT INTO t_axii_coin (id_coin, simbolo, blockchain, preco_mercado)
VALUES ('10000000-0000-4000-8000-000000000003', 'ETH', 'Ethereum', 18500.00);

INSERT INTO t_axii_coin (id_coin, simbolo, blockchain, preco_mercado)
VALUES ('10000000-0000-4000-8000-000000000004', 'SOL', 'Solana', 950.00);

INSERT INTO t_axii_coin (id_coin, simbolo, blockchain, preco_mercado)
VALUES ('10000000-0000-4000-8000-000000000006', 'BTC', 'Bitcoin', 350000.00);

-- STABLECOIN (subtipo) - usa o mesmo id do CRYPTO_ASSET correspondente
INSERT INTO t_axii_stablecoin (id_stablecoin, moeda_referencia, taxa_conversao)
VALUES ('10000000-0000-4000-8000-000000000002', 'USD', 5.4500);

INSERT INTO t_axii_stablecoin (id_stablecoin, moeda_referencia, taxa_conversao)
VALUES ('10000000-0000-4000-8000-000000000005', 'USD', 5.4500);

INSERT INTO t_axii_stablecoin (id_stablecoin, moeda_referencia, taxa_conversao)
VALUES ('10000000-0000-4000-8000-000000000007', 'USD', 5.4500);

COMMIT;


-- ---------------------------------------------------------------------
-- 2. UPDATE
-- ---------------------------------------------------------------------

-- Usuária Ana atualizou o nome completo
UPDATE t_axii_user
   SET nome = 'Ana Souza Pereira',
       data_atualizacao = SYSTIMESTAMP
 WHERE id_user = 'a0000000-0000-4000-8000-000000000001';

-- Usuário Bruno ativou o modo escuro (localiza a configuração pela FK do usuário)
UPDATE t_axii_settings
   SET modo_escuro = 1
 WHERE id_settings = (SELECT id_settings
                        FROM t_axii_user
                       WHERE email = 'bruno.lima@email.com');

-- Usuário Diego desativou a notificação de variação de preço
UPDATE t_axii_notifications
   SET variacao_preco = 0
 WHERE id_notifications = 'c0000000-0000-4000-8000-000000000004';

-- Usuária Ana desativou a conta do Nubank
UPDATE t_axii_bank
   SET ativo = 0
 WHERE id_bank = 'e0000000-0000-4000-8000-000000000002';

-- Atualização do preço de mercado do Bitcoin (todas as moedas BTC)
UPDATE t_axii_coin
   SET preco_mercado = 362500.00
 WHERE simbolo = 'BTC';

-- Usuária Carla comprou mais 5 Solana
UPDATE t_axii_crypto_asset
   SET quantidade = quantidade + 5
 WHERE id_crypto_asset = '10000000-0000-4000-8000-000000000004';

-- Atualização da taxa de conversão das stablecoins atreladas ao dólar
UPDATE t_axii_stablecoin
   SET taxa_conversao = 5.5200
 WHERE moeda_referencia = 'USD';

COMMIT;


-- ---------------------------------------------------------------------
-- 3. DELETE
-- ---------------------------------------------------------------------

-- Usuário Diego removeu a chave Pix do tipo CNPJ
DELETE FROM t_axii_pix_key
 WHERE id_pix_key = 'f0000000-0000-4000-8000-000000000004';

-- Exclusão da usuária Eduarda.
DELETE FROM t_axii_user
 WHERE id_user = 'a0000000-0000-4000-8000-000000000005';

-- NO ACTION: Settings, Notifications e Identity só podem ser removidos
-- depois do usuário, pois é o usuário que referencia essas tabelas.
DELETE FROM t_axii_settings
 WHERE id_settings = 'b0000000-0000-4000-8000-000000000005';

DELETE FROM t_axii_notifications
 WHERE id_notifications = 'c0000000-0000-4000-8000-000000000005';

DELETE FROM t_axii_identity
 WHERE id_identity = 'd0000000-0000-4000-8000-000000000005';

COMMIT;


-- ---------------------------------------------------------------------
-- 4. SELECT
-- ---------------------------------------------------------------------

SELECT * FROM t_axii_user ORDER BY nome;
SELECT * FROM t_axii_settings;
SELECT * FROM t_axii_notifications;
SELECT * FROM t_axii_identity;
SELECT * FROM t_axii_bank;
SELECT * FROM t_axii_pix_key;
SELECT * FROM t_axii_crypto_asset ORDER BY data_aquisicao;
SELECT * FROM t_axii_coin;
SELECT * FROM t_axii_stablecoin;

-- Usuários com suas configurações, notificações e identidade (relacionamentos 1:1)
SELECT u.nome,
       u.email,
       i.cpf,
       i.telefone,
       s.modo_escuro,
       s.idioma,
       n.transacao,
       n.variacao_preco,
       n.marketing
  FROM t_axii_user u
  JOIN t_axii_settings      s ON s.id_settings      = u.id_settings
  JOIN t_axii_notifications n ON n.id_notifications = u.id_notifications
  JOIN t_axii_identity      i ON i.id_identity      = u.id_identity
 ORDER BY u.nome;

-- Contas bancárias ativas de cada usuário
SELECT u.nome,
       b.nome_banco,
       b.agencia,
       b.numero_conta
  FROM t_axii_bank b
  JOIN t_axii_user u ON u.id_user = b.id_user
 WHERE b.ativo = 1
 ORDER BY u.nome, b.nome_banco;

-- Chaves Pix de cada usuário
SELECT u.nome,
       p.tipo,
       p.chave
  FROM t_axii_pix_key p
  JOIN t_axii_user u ON u.id_user = p.id_user
 ORDER BY u.nome;

-- Moedas (COIN) de cada usuário com o valor total em BRL
SELECT u.nome,
       ca.ativo,
       c.simbolo,
       c.blockchain,
       ca.quantidade,
       c.preco_mercado,
       ROUND(ca.quantidade * c.preco_mercado, 2) AS valor_total_brl
  FROM t_axii_crypto_asset ca
  JOIN t_axii_coin c ON c.id_coin = ca.id_crypto_asset
  JOIN t_axii_user u ON u.id_user = ca.id_user
 ORDER BY valor_total_brl DESC;

-- Stablecoins de cada usuário com o valor convertido em BRL
SELECT u.nome,
       ca.ativo,
       s.moeda_referencia,
       ca.quantidade,
       s.taxa_conversao,
       ROUND(ca.quantidade * s.taxa_conversao, 2) AS valor_total_brl
  FROM t_axii_crypto_asset ca
  JOIN t_axii_stablecoin s ON s.id_stablecoin = ca.id_crypto_asset
  JOIN t_axii_user u ON u.id_user = ca.id_user
 ORDER BY u.nome;

-- Patrimônio total em cripto por usuário (moedas + stablecoins)
SELECT u.nome,
       COUNT(ca.id_crypto_asset) AS qtd_ativos,
       ROUND(SUM(ca.quantidade * COALESCE(c.preco_mercado, s.taxa_conversao)), 2) AS patrimonio_brl
  FROM t_axii_user u
  JOIN t_axii_crypto_asset ca ON ca.id_user = u.id_user
  LEFT JOIN t_axii_coin       c ON c.id_coin       = ca.id_crypto_asset
  LEFT JOIN t_axii_stablecoin s ON s.id_stablecoin = ca.id_crypto_asset
 GROUP BY u.nome
 ORDER BY patrimonio_brl DESC;

-- Conferência do DELETE: a usuária Eduarda e seus dados dependentes não existem mais
SELECT COUNT(*) AS qtd_usuarios  FROM t_axii_user         WHERE id_user = 'a0000000-0000-4000-8000-000000000005';
SELECT COUNT(*) AS qtd_contas    FROM t_axii_bank         WHERE id_user = 'a0000000-0000-4000-8000-000000000005';
SELECT COUNT(*) AS qtd_ativos    FROM t_axii_crypto_asset WHERE id_user = 'a0000000-0000-4000-8000-000000000005';
SELECT COUNT(*) AS qtd_stablecoin FROM t_axii_stablecoin  WHERE id_stablecoin = '10000000-0000-4000-8000-000000000007';
