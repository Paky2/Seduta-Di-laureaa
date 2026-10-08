--
-- PostgreSQL database dump
--

\restrict LYBRNv6WM8QvPAMuKUaiNRN51zBhux82EBvTplatLHdG7XQc4eVaWpCetkciEAx

-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

-- Started on 2026-10-08 17:05:32

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 872 (class 1247 OID 52283)
-- Name: stato_richiesta; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.stato_richiesta AS ENUM (
    'ACCETTATA',
    'RIFIUTATA',
    'PENDENTE'
);


ALTER TYPE public.stato_richiesta OWNER TO postgres;

--
-- TOC entry 878 (class 1247 OID 52296)
-- Name: stato_tesi; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.stato_tesi AS ENUM (
    'IN_CORSO',
    'FINITA',
    'ACCETTATA',
    'RIFIUTATA'
);


ALTER TYPE public.stato_tesi OWNER TO postgres;

--
-- TOC entry 875 (class 1247 OID 52290)
-- Name: stato_tirocinio; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.stato_tirocinio AS ENUM (
    'IN_CORSO',
    'FINITO'
);


ALTER TYPE public.stato_tirocinio OWNER TO postgres;

--
-- TOC entry 881 (class 1247 OID 52306)
-- Name: tipo_tirocinio; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_tirocinio AS ENUM (
    'INTERNO',
    'ESTERNO'
);


ALTER TYPE public.tipo_tirocinio OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 238 (class 1259 OID 52484)
-- Name: commissione; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commissione (
    id integer NOT NULL,
    id_seduta integer NOT NULL
);


ALTER TABLE public.commissione OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 52499)
-- Name: commissione_docente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commissione_docente (
    id_commissione integer NOT NULL,
    id_docente integer NOT NULL
);


ALTER TABLE public.commissione_docente OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 52483)
-- Name: commissione_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.commissione_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.commissione_id_seq OWNER TO postgres;

--
-- TOC entry 5058 (class 0 OID 0)
-- Dependencies: 237
-- Name: commissione_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.commissione_id_seq OWNED BY public.commissione.id;


--
-- TOC entry 226 (class 1259 OID 52360)
-- Name: coordinatore; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coordinatore (
    id_docente integer NOT NULL
);


ALTER TABLE public.coordinatore OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 52345)
-- Name: docente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.docente (
    id_docente integer NOT NULL,
    id_utente integer NOT NULL
);


ALTER TABLE public.docente OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 52344)
-- Name: docente_id_docente_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.docente_id_docente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.docente_id_docente_seq OWNER TO postgres;

--
-- TOC entry 5059 (class 0 OID 0)
-- Dependencies: 224
-- Name: docente_id_docente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.docente_id_docente_seq OWNED BY public.docente.id_docente;


--
-- TOC entry 220 (class 1259 OID 33003)
-- Name: prenotazione_seduta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prenotazione_seduta (
    id integer NOT NULL,
    matricola_studente character varying(50) NOT NULL,
    id_seduta integer NOT NULL
);


ALTER TABLE public.prenotazione_seduta OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 33002)
-- Name: prenotazione_seduta_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.prenotazione_seduta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.prenotazione_seduta_id_seq OWNER TO postgres;

--
-- TOC entry 5060 (class 0 OID 0)
-- Dependencies: 219
-- Name: prenotazione_seduta_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.prenotazione_seduta_id_seq OWNED BY public.prenotazione_seduta.id;


--
-- TOC entry 232 (class 1259 OID 52412)
-- Name: richiesta_tirocinio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.richiesta_tirocinio (
    id integer NOT NULL,
    stato public.stato_richiesta NOT NULL,
    matricola_studente character varying(50) NOT NULL,
    id_tirocinio integer NOT NULL,
    id_docente integer NOT NULL
);


ALTER TABLE public.richiesta_tirocinio OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 52411)
-- Name: richiesta_tirocinio_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.richiesta_tirocinio_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.richiesta_tirocinio_id_seq OWNER TO postgres;

--
-- TOC entry 5061 (class 0 OID 0)
-- Dependencies: 231
-- Name: richiesta_tirocinio_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.richiesta_tirocinio_id_seq OWNED BY public.richiesta_tirocinio.id;


--
-- TOC entry 234 (class 1259 OID 52439)
-- Name: seduta_laurea; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seduta_laurea (
    id integer NOT NULL,
    data date NOT NULL,
    ora time without time zone NOT NULL,
    luogo character varying(255) NOT NULL,
    disponibilita boolean DEFAULT true NOT NULL
);


ALTER TABLE public.seduta_laurea OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 52438)
-- Name: seduta_laurea_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seduta_laurea_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.seduta_laurea_id_seq OWNER TO postgres;

--
-- TOC entry 5062 (class 0 OID 0)
-- Dependencies: 233
-- Name: seduta_laurea_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.seduta_laurea_id_seq OWNED BY public.seduta_laurea.id;


--
-- TOC entry 223 (class 1259 OID 52330)
-- Name: studente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.studente (
    matricola character varying(50) NOT NULL,
    id_utente integer NOT NULL
);


ALTER TABLE public.studente OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 52452)
-- Name: tesi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tesi (
    id integer NOT NULL,
    stato public.stato_tesi NOT NULL,
    file character varying(500) NOT NULL,
    versione integer DEFAULT 1 NOT NULL,
    matricola_studente character varying(50) NOT NULL,
    id_relatore integer NOT NULL,
    id_seduta integer NOT NULL
);


ALTER TABLE public.tesi OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 52451)
-- Name: tesi_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tesi_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tesi_id_seq OWNER TO postgres;

--
-- TOC entry 5063 (class 0 OID 0)
-- Dependencies: 235
-- Name: tesi_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tesi_id_seq OWNED BY public.tesi.id;


--
-- TOC entry 228 (class 1259 OID 52372)
-- Name: tirocinio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tirocinio (
    id integer NOT NULL,
    argomento character varying(255) NOT NULL,
    id_docente_responsabile integer NOT NULL,
    stato public.stato_tirocinio NOT NULL,
    tipo public.tipo_tirocinio NOT NULL
);


ALTER TABLE public.tirocinio OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 52399)
-- Name: tirocinio_esterno; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tirocinio_esterno (
    id_tirocinio integer NOT NULL,
    nominativo_referente_aziendale character varying(150) NOT NULL
);


ALTER TABLE public.tirocinio_esterno OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 52371)
-- Name: tirocinio_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tirocinio_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tirocinio_id_seq OWNER TO postgres;

--
-- TOC entry 5064 (class 0 OID 0)
-- Dependencies: 227
-- Name: tirocinio_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tirocinio_id_seq OWNED BY public.tirocinio.id;


--
-- TOC entry 229 (class 1259 OID 52388)
-- Name: tirocinio_interno; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tirocinio_interno (
    id_tirocinio integer NOT NULL
);


ALTER TABLE public.tirocinio_interno OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 52312)
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utente (
    id integer NOT NULL,
    nome character varying(100) NOT NULL,
    cognome character varying(100) NOT NULL,
    email character varying(150) NOT NULL,
    login character varying(100) NOT NULL,
    password character varying(255) NOT NULL
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 52311)
-- Name: utente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utente_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utente_id_seq OWNER TO postgres;

--
-- TOC entry 5065 (class 0 OID 0)
-- Dependencies: 221
-- Name: utente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utente_id_seq OWNED BY public.utente.id;


--
-- TOC entry 4831 (class 2604 OID 52487)
-- Name: commissione id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commissione ALTER COLUMN id SET DEFAULT nextval('public.commissione_id_seq'::regclass);


--
-- TOC entry 4824 (class 2604 OID 52348)
-- Name: docente id_docente; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.docente ALTER COLUMN id_docente SET DEFAULT nextval('public.docente_id_docente_seq'::regclass);


--
-- TOC entry 4822 (class 2604 OID 33006)
-- Name: prenotazione_seduta id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prenotazione_seduta ALTER COLUMN id SET DEFAULT nextval('public.prenotazione_seduta_id_seq'::regclass);


--
-- TOC entry 4826 (class 2604 OID 52415)
-- Name: richiesta_tirocinio id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.richiesta_tirocinio ALTER COLUMN id SET DEFAULT nextval('public.richiesta_tirocinio_id_seq'::regclass);


--
-- TOC entry 4827 (class 2604 OID 52442)
-- Name: seduta_laurea id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seduta_laurea ALTER COLUMN id SET DEFAULT nextval('public.seduta_laurea_id_seq'::regclass);


--
-- TOC entry 4829 (class 2604 OID 52455)
-- Name: tesi id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tesi ALTER COLUMN id SET DEFAULT nextval('public.tesi_id_seq'::regclass);


--
-- TOC entry 4825 (class 2604 OID 52375)
-- Name: tirocinio id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio ALTER COLUMN id SET DEFAULT nextval('public.tirocinio_id_seq'::regclass);


--
-- TOC entry 4823 (class 2604 OID 52315)
-- Name: utente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente ALTER COLUMN id SET DEFAULT nextval('public.utente_id_seq'::regclass);


--
-- TOC entry 5051 (class 0 OID 52484)
-- Dependencies: 238
-- Data for Name: commissione; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commissione (id, id_seduta) FROM stdin;
1	1
\.


--
-- TOC entry 5052 (class 0 OID 52499)
-- Dependencies: 239
-- Data for Name: commissione_docente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commissione_docente (id_commissione, id_docente) FROM stdin;
1	1
\.


--
-- TOC entry 5039 (class 0 OID 52360)
-- Dependencies: 226
-- Data for Name: coordinatore; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coordinatore (id_docente) FROM stdin;
1
\.


--
-- TOC entry 5038 (class 0 OID 52345)
-- Dependencies: 225
-- Data for Name: docente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.docente (id_docente, id_utente) FROM stdin;
1	2
2	4
3	6
\.


--
-- TOC entry 5033 (class 0 OID 33003)
-- Dependencies: 220
-- Data for Name: prenotazione_seduta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prenotazione_seduta (id, matricola_studente, id_seduta) FROM stdin;
1	SP001	1
13	SP001	2
\.


--
-- TOC entry 5045 (class 0 OID 52412)
-- Dependencies: 232
-- Data for Name: richiesta_tirocinio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.richiesta_tirocinio (id, stato, matricola_studente, id_tirocinio, id_docente) FROM stdin;
1	PENDENTE	SP001	1	1
2	ACCETTATA	3323	1	1
\.


--
-- TOC entry 5047 (class 0 OID 52439)
-- Dependencies: 234
-- Data for Name: seduta_laurea; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.seduta_laurea (id, data, ora, luogo, disponibilita) FROM stdin;
1	2026-10-15	10:00:00	Aula Magna	t
2	2026-11-20	11:00:00	Aula Magna	t
\.


--
-- TOC entry 5036 (class 0 OID 52330)
-- Dependencies: 223
-- Data for Name: studente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.studente (matricola, id_utente) FROM stdin;
SP001	1
3323	3
32323	5
33234	7
4224	8
12	11
646	12
uet	16
575	17
\.


--
-- TOC entry 5049 (class 0 OID 52452)
-- Dependencies: 236
-- Data for Name: tesi; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tesi (id, stato, file, versione, matricola_studente, id_relatore, id_seduta) FROM stdin;
1	IN_CORSO	tesi_pieropp.pdf	1	SP001	1	1
\.


--
-- TOC entry 5041 (class 0 OID 52372)
-- Dependencies: 228
-- Data for Name: tirocinio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tirocinio (id, argomento, id_docente_responsabile, stato, tipo) FROM stdin;
1	Sviluppo applicazioni Java	1	IN_CORSO	INTERNO
2	Pollo	3	IN_CORSO	INTERNO
\.


--
-- TOC entry 5043 (class 0 OID 52399)
-- Dependencies: 230
-- Data for Name: tirocinio_esterno; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tirocinio_esterno (id_tirocinio, nominativo_referente_aziendale) FROM stdin;
\.


--
-- TOC entry 5042 (class 0 OID 52388)
-- Dependencies: 229
-- Data for Name: tirocinio_interno; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tirocinio_interno (id_tirocinio) FROM stdin;
1
2
\.


--
-- TOC entry 5035 (class 0 OID 52312)
-- Dependencies: 222
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utente (id, nome, cognome, email, login, password) FROM stdin;
1	Piero	PP	pieropp@email.it	PieroPP	1234
2	Ciro	E	ciroe@email.it	CiroE	1234
3	Pippo	baudo	@pio	PippoBaudo	22
4	Pippa	Bauda	@pippa	PippaBauda	22
5	w	w	w	Pippo	22
6	w	ww	ww	Pippa	22
7	we	we	wq	e	22
8	s	sd	sd	ee	22
11	w	ww	wwe	eee	22
12	ui	uuy	uyt	oi	22
16	yjr	jst	j	eet	22
17	oi	ky	ui	poi	22
\.


--
-- TOC entry 5066 (class 0 OID 0)
-- Dependencies: 237
-- Name: commissione_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.commissione_id_seq', 1, true);


--
-- TOC entry 5067 (class 0 OID 0)
-- Dependencies: 224
-- Name: docente_id_docente_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.docente_id_docente_seq', 3, true);


--
-- TOC entry 5068 (class 0 OID 0)
-- Dependencies: 219
-- Name: prenotazione_seduta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.prenotazione_seduta_id_seq', 18, true);


--
-- TOC entry 5069 (class 0 OID 0)
-- Dependencies: 231
-- Name: richiesta_tirocinio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.richiesta_tirocinio_id_seq', 2, true);


--
-- TOC entry 5070 (class 0 OID 0)
-- Dependencies: 233
-- Name: seduta_laurea_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seduta_laurea_id_seq', 2, true);


--
-- TOC entry 5071 (class 0 OID 0)
-- Dependencies: 235
-- Name: tesi_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tesi_id_seq', 1, true);


--
-- TOC entry 5072 (class 0 OID 0)
-- Dependencies: 227
-- Name: tirocinio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tirocinio_id_seq', 2, true);


--
-- TOC entry 5073 (class 0 OID 0)
-- Dependencies: 221
-- Name: utente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.utente_id_seq', 17, true);


--
-- TOC entry 4869 (class 2606 OID 52505)
-- Name: commissione_docente commissione_docente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commissione_docente
    ADD CONSTRAINT commissione_docente_pkey PRIMARY KEY (id_commissione, id_docente);


--
-- TOC entry 4865 (class 2606 OID 52493)
-- Name: commissione commissione_id_seduta_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commissione
    ADD CONSTRAINT commissione_id_seduta_key UNIQUE (id_seduta);


--
-- TOC entry 4867 (class 2606 OID 52491)
-- Name: commissione commissione_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commissione
    ADD CONSTRAINT commissione_pkey PRIMARY KEY (id);


--
-- TOC entry 4851 (class 2606 OID 52365)
-- Name: coordinatore coordinatore_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coordinatore
    ADD CONSTRAINT coordinatore_pkey PRIMARY KEY (id_docente);


--
-- TOC entry 4847 (class 2606 OID 52354)
-- Name: docente docente_id_utente_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.docente
    ADD CONSTRAINT docente_id_utente_key UNIQUE (id_utente);


--
-- TOC entry 4849 (class 2606 OID 52352)
-- Name: docente docente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.docente
    ADD CONSTRAINT docente_pkey PRIMARY KEY (id_docente);


--
-- TOC entry 4833 (class 2606 OID 33013)
-- Name: prenotazione_seduta prenotazione_seduta_id_seduta_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prenotazione_seduta
    ADD CONSTRAINT prenotazione_seduta_id_seduta_key UNIQUE (id_seduta);


--
-- TOC entry 4835 (class 2606 OID 33011)
-- Name: prenotazione_seduta prenotazione_seduta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prenotazione_seduta
    ADD CONSTRAINT prenotazione_seduta_pkey PRIMARY KEY (id);


--
-- TOC entry 4859 (class 2606 OID 52422)
-- Name: richiesta_tirocinio richiesta_tirocinio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.richiesta_tirocinio
    ADD CONSTRAINT richiesta_tirocinio_pkey PRIMARY KEY (id);


--
-- TOC entry 4861 (class 2606 OID 52450)
-- Name: seduta_laurea seduta_laurea_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seduta_laurea
    ADD CONSTRAINT seduta_laurea_pkey PRIMARY KEY (id);


--
-- TOC entry 4843 (class 2606 OID 52338)
-- Name: studente studente_id_utente_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studente
    ADD CONSTRAINT studente_id_utente_key UNIQUE (id_utente);


--
-- TOC entry 4845 (class 2606 OID 52336)
-- Name: studente studente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studente
    ADD CONSTRAINT studente_pkey PRIMARY KEY (matricola);


--
-- TOC entry 4863 (class 2606 OID 52467)
-- Name: tesi tesi_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tesi
    ADD CONSTRAINT tesi_pkey PRIMARY KEY (id);


--
-- TOC entry 4857 (class 2606 OID 52405)
-- Name: tirocinio_esterno tirocinio_esterno_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio_esterno
    ADD CONSTRAINT tirocinio_esterno_pkey PRIMARY KEY (id_tirocinio);


--
-- TOC entry 4855 (class 2606 OID 52393)
-- Name: tirocinio_interno tirocinio_interno_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio_interno
    ADD CONSTRAINT tirocinio_interno_pkey PRIMARY KEY (id_tirocinio);


--
-- TOC entry 4853 (class 2606 OID 52382)
-- Name: tirocinio tirocinio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio
    ADD CONSTRAINT tirocinio_pkey PRIMARY KEY (id);


--
-- TOC entry 4837 (class 2606 OID 52327)
-- Name: utente utente_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_email_key UNIQUE (email);


--
-- TOC entry 4839 (class 2606 OID 52329)
-- Name: utente utente_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_login_key UNIQUE (login);


--
-- TOC entry 4841 (class 2606 OID 52325)
-- Name: utente utente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);


--
-- TOC entry 4883 (class 2606 OID 52506)
-- Name: commissione_docente fk_commissione_docente_commissione; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commissione_docente
    ADD CONSTRAINT fk_commissione_docente_commissione FOREIGN KEY (id_commissione) REFERENCES public.commissione(id) ON DELETE CASCADE;


--
-- TOC entry 4884 (class 2606 OID 52511)
-- Name: commissione_docente fk_commissione_docente_docente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commissione_docente
    ADD CONSTRAINT fk_commissione_docente_docente FOREIGN KEY (id_docente) REFERENCES public.docente(id_docente) ON DELETE CASCADE;


--
-- TOC entry 4882 (class 2606 OID 52494)
-- Name: commissione fk_commissione_seduta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commissione
    ADD CONSTRAINT fk_commissione_seduta FOREIGN KEY (id_seduta) REFERENCES public.seduta_laurea(id) ON DELETE CASCADE;


--
-- TOC entry 4872 (class 2606 OID 52366)
-- Name: coordinatore fk_coordinatore_docente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coordinatore
    ADD CONSTRAINT fk_coordinatore_docente FOREIGN KEY (id_docente) REFERENCES public.docente(id_docente) ON DELETE CASCADE;


--
-- TOC entry 4871 (class 2606 OID 52355)
-- Name: docente fk_docente_utente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.docente
    ADD CONSTRAINT fk_docente_utente FOREIGN KEY (id_utente) REFERENCES public.utente(id) ON DELETE CASCADE;


--
-- TOC entry 4876 (class 2606 OID 52433)
-- Name: richiesta_tirocinio fk_richiesta_docente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.richiesta_tirocinio
    ADD CONSTRAINT fk_richiesta_docente FOREIGN KEY (id_docente) REFERENCES public.docente(id_docente);


--
-- TOC entry 4877 (class 2606 OID 52423)
-- Name: richiesta_tirocinio fk_richiesta_studente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.richiesta_tirocinio
    ADD CONSTRAINT fk_richiesta_studente FOREIGN KEY (matricola_studente) REFERENCES public.studente(matricola);


--
-- TOC entry 4878 (class 2606 OID 52428)
-- Name: richiesta_tirocinio fk_richiesta_tirocinio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.richiesta_tirocinio
    ADD CONSTRAINT fk_richiesta_tirocinio FOREIGN KEY (id_tirocinio) REFERENCES public.tirocinio(id);


--
-- TOC entry 4870 (class 2606 OID 52339)
-- Name: studente fk_studente_utente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studente
    ADD CONSTRAINT fk_studente_utente FOREIGN KEY (id_utente) REFERENCES public.utente(id) ON DELETE CASCADE;


--
-- TOC entry 4879 (class 2606 OID 52473)
-- Name: tesi fk_tesi_relatore; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tesi
    ADD CONSTRAINT fk_tesi_relatore FOREIGN KEY (id_relatore) REFERENCES public.docente(id_docente);


--
-- TOC entry 4880 (class 2606 OID 52478)
-- Name: tesi fk_tesi_seduta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tesi
    ADD CONSTRAINT fk_tesi_seduta FOREIGN KEY (id_seduta) REFERENCES public.seduta_laurea(id);


--
-- TOC entry 4881 (class 2606 OID 52468)
-- Name: tesi fk_tesi_studente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tesi
    ADD CONSTRAINT fk_tesi_studente FOREIGN KEY (matricola_studente) REFERENCES public.studente(matricola);


--
-- TOC entry 4873 (class 2606 OID 52383)
-- Name: tirocinio fk_tirocinio_docente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio
    ADD CONSTRAINT fk_tirocinio_docente FOREIGN KEY (id_docente_responsabile) REFERENCES public.docente(id_docente);


--
-- TOC entry 4875 (class 2606 OID 52406)
-- Name: tirocinio_esterno fk_tirocinio_esterno; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio_esterno
    ADD CONSTRAINT fk_tirocinio_esterno FOREIGN KEY (id_tirocinio) REFERENCES public.tirocinio(id) ON DELETE CASCADE;


--
-- TOC entry 4874 (class 2606 OID 52394)
-- Name: tirocinio_interno fk_tirocinio_interno; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tirocinio_interno
    ADD CONSTRAINT fk_tirocinio_interno FOREIGN KEY (id_tirocinio) REFERENCES public.tirocinio(id) ON DELETE CASCADE;


-- Completed on 2026-10-08 17:05:34

--
-- PostgreSQL database dump complete
--

\unrestrict LYBRNv6WM8QvPAMuKUaiNRN51zBhux82EBvTplatLHdG7XQc4eVaWpCetkciEAx

