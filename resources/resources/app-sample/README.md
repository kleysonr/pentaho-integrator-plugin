Antes de usar http://localhost:1010/upload insira o token na tabela
INSERT INTO tokens_bi(username, token, url) VALUES ('admin', '1436991744559', 'L3BlbnRhaG8vcGx1Z2luL3VwbG9hZGZpbGUvYXBpL3NlbmQ=');

A cada chamada o token eh consumido, entao antes de cada chamada no form o token acima precisa ser inserido.