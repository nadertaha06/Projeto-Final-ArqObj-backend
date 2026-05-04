
    create table avaliacoes (
        nota integer not null,
        cliente_id bigint not null,
        data_criacao datetime(6) not null,
        id bigint not null auto_increment,
        produto_id bigint not null,
        comentario varchar(1000),
        primary key (id),
        check ((nota<=5) and (nota>=1))
    ) engine=InnoDB;

    create table carrinhos (
        cliente_id bigint not null,
        id bigint not null auto_increment,
        primary key (id)
    ) engine=InnoDB;

    create table categorias (
        id bigint not null auto_increment,
        descricao varchar(255),
        nome varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table clientes (
        id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table cupons (
        ativo bit not null,
        data_validade date not null,
        desconto decimal(38,2) not null,
        uso_atual integer not null,
        uso_maximo integer not null,
        id bigint not null auto_increment,
        produto_id bigint not null,
        codigo varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table descontos (
        id bigint not null auto_increment,
        descricao varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table descontos_fixo (
        valor_fixo decimal(38,2) not null,
        id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table descontos_percentual (
        percentual decimal(38,2) not null,
        id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table enderecos (
        estado varchar(2) not null,
        cep varchar(8) not null,
        cliente_id bigint not null,
        id bigint not null auto_increment,
        bairro varchar(255) not null,
        cidade varchar(255) not null,
        complemento varchar(255),
        numero varchar(255) not null,
        rua varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table entregas (
        previsao_entrega date,
        data_entrega datetime(6),
        endereco_id bigint not null,
        id bigint not null auto_increment,
        pedido_id bigint not null,
        codigo_rastreio varchar(255),
        status varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table estoques (
        quantidade integer not null,
        quantidade_minima integer not null,
        id bigint not null auto_increment,
        produto_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table itens_carrinho (
        quantidade integer not null,
        carrinho_id bigint not null,
        id bigint not null auto_increment,
        produto_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table itens_pedido (
        preco_unitario decimal(38,2) not null,
        quantidade integer not null,
        id bigint not null auto_increment,
        pedido_id bigint not null,
        produto_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table pagamentos (
        valor decimal(38,2) not null,
        data_pagamento datetime(6),
        id bigint not null auto_increment,
        status varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table pagamentos_boleto (
        data_vencimento date,
        id bigint not null,
        codigo_barras varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pagamentos_cartao (
        parcelas integer,
        ultimos_quatro_digitos varchar(4),
        id bigint not null,
        nome_titular varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pagamentos_pix (
        id bigint not null,
        chave_pix varchar(255),
        txid varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table pedidos (
        valor_total decimal(38,2) not null,
        cliente_id bigint not null,
        data_criacao datetime(6) not null,
        id bigint not null auto_increment,
        pagamento_id bigint,
        status enum ('AGUARDANDO_PAGAMENTO','CANCELADO','EM_PREPARACAO','ENTREGUE','ENVIADO','PAGO') not null,
        primary key (id)
    ) engine=InnoDB;

    create table produtos (
        ativo bit not null,
        preco decimal(38,2) not null,
        categoria_id bigint,
        id bigint not null auto_increment,
        vendedor_id bigint not null,
        descricao varchar(2000),
        imagem_url varchar(255),
        nome varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table usuarios (
        data_cadastro datetime(6) not null,
        id bigint not null auto_increment,
        cpf varchar(11) not null,
        email varchar(255) not null,
        nome varchar(255) not null,
        senha varchar(255) not null,
        telefone varchar(255),
        tipo enum ('CLIENTE','VENDEDOR') not null,
        primary key (id)
    ) engine=InnoDB;

    create table vendedores (
        avaliacao float(53) not null,
        id bigint not null,
        nome_loja varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    alter table avaliacoes 
       add constraint uk_avaliacao_cliente_produto unique (cliente_id, produto_id);

    alter table carrinhos 
       add constraint UKj07qipjcp540o0skbx4gwjqfh unique (cliente_id);

    alter table categorias 
       add constraint UK9qte5svl2i6n82lpdyyheoi1h unique (nome);

    alter table cupons 
       add constraint UKfwxbn8wj3xijqpw9x0hevuba6 unique (codigo);

    alter table entregas 
       add constraint UKdx3b912ojw96ry1s3uunl9wvq unique (pedido_id);

    alter table estoques 
       add constraint UKkcsbra62r4qnap50cjmh5l74e unique (produto_id);

    alter table pedidos 
       add constraint UKl5qt79iitwrc4g9vy5lh01de1 unique (pagamento_id);

    alter table usuarios 
       add constraint UK2et2smpfrtsohr7w9fe1v8a5e unique (cpf);

    alter table usuarios 
       add constraint UKkfsp0s1tflm1cwlj8idhqsad0 unique (email);

    alter table avaliacoes 
       add constraint FKkgp24xf7hsebxvhrr0yn947s4 
       foreign key (cliente_id) 
       references clientes (id);

    alter table avaliacoes 
       add constraint FK2gyp99eufrcum0w75vlxjk61i 
       foreign key (produto_id) 
       references produtos (id);

    alter table carrinhos 
       add constraint FK4uilxnmbmc8a1hj5l4irdx39r 
       foreign key (cliente_id) 
       references clientes (id);

    alter table clientes 
       add constraint FKl8la8bmm5w5vtntkh5bhv5y41 
       foreign key (id) 
       references usuarios (id);

    alter table cupons 
       add constraint FKjac1h17lnuqj2ec0c8rjl4noq 
       foreign key (produto_id) 
       references produtos (id);

    alter table descontos_fixo 
       add constraint FKj2c5qmp5551fqww2jx9f32wdw 
       foreign key (id) 
       references descontos (id);

    alter table descontos_percentual 
       add constraint FKn2hivpwbpoi1lwfy66k2tet7k 
       foreign key (id) 
       references descontos (id);

    alter table enderecos 
       add constraint FKifst74rmma55fhyky5g1l0589 
       foreign key (cliente_id) 
       references clientes (id);

    alter table entregas 
       add constraint FKj8bn3iv40jeatastjcskf4rue 
       foreign key (endereco_id) 
       references enderecos (id);

    alter table entregas 
       add constraint FKrtxf2mg1t04ohe5icx54lmidh 
       foreign key (pedido_id) 
       references pedidos (id);

    alter table estoques 
       add constraint FK3jnnltuci1c3qpgst0jjnxq18 
       foreign key (produto_id) 
       references produtos (id);

    alter table itens_carrinho 
       add constraint FKquesql8eqm9u5cehaa121s88j 
       foreign key (carrinho_id) 
       references carrinhos (id);

    alter table itens_carrinho 
       add constraint FKas29vkil8nc305nkhao3d9h0b 
       foreign key (produto_id) 
       references produtos (id);

    alter table itens_pedido 
       add constraint FK42mycompce3b7yt3l6ukdwsxy 
       foreign key (pedido_id) 
       references pedidos (id);

    alter table itens_pedido 
       add constraint FKxytdlekpdaobqphujy9bmuhl 
       foreign key (produto_id) 
       references produtos (id);

    alter table pagamentos_boleto 
       add constraint FK5g015ibxvcss6wsf2uqn7ogn 
       foreign key (id) 
       references pagamentos (id);

    alter table pagamentos_cartao 
       add constraint FK81utkhsxc0snrgcsqtaw8j54x 
       foreign key (id) 
       references pagamentos (id);

    alter table pagamentos_pix 
       add constraint FKb91302qugnsg3o23o0dxrk130 
       foreign key (id) 
       references pagamentos (id);

    alter table pedidos 
       add constraint FKg7202lk0hwxn04bmdl2thth5b 
       foreign key (cliente_id) 
       references clientes (id);

    alter table pedidos 
       add constraint FKqwulxlrqb743xeqef8b0fmkqj 
       foreign key (pagamento_id) 
       references pagamentos (id);

    alter table produtos 
       add constraint FK8rqw0ljwdaom34jr2t46bjtrn 
       foreign key (categoria_id) 
       references categorias (id);

    alter table produtos 
       add constraint FKjst8rt9yga63q9l5uxuhxaq8f 
       foreign key (vendedor_id) 
       references vendedores (id);

    alter table vendedores 
       add constraint FK2xmy56jd595f3u1iybwyu1tas 
       foreign key (id) 
       references usuarios (id);
