--
-- PostgreSQL database dump
--

\restrict 0dnFfHfGQVOw7jKmB8S5VBixC0wpXhbgh346wf1k9RGodZsd0KWeQX5y42eCmuF

-- Dumped from database version 16.10 (Ubuntu 16.10-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.10 (Ubuntu 16.10-0ubuntu0.24.04.1)

-- Started on 2025-11-03 12:01:32 CST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 218 (class 1259 OID 57443)
-- Name: items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.items (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    description text,
    price numeric(10,2) NOT NULL,
    current_price numeric(10,2) NOT NULL,
    original_price numeric(10,2) NOT NULL,
    available boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.items OWNER TO postgres;

--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN items.current_price; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.items.current_price IS 'Current highest offer price or original price';


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN items.original_price; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.items.original_price IS 'Initial listing price';


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN items.available; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.items.available IS 'Whether item is still available for offers';


--
-- TOC entry 217 (class 1259 OID 57442)
-- Name: items_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.items_id_seq OWNER TO postgres;

--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 217
-- Name: items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.items_id_seq OWNED BY public.items.id;


--
-- TOC entry 220 (class 1259 OID 57455)
-- Name: offers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offers (
    id bigint NOT NULL,
    item_id bigint NOT NULL,
    user_id bigint NOT NULL,
    offer_amount numeric(10,2) NOT NULL,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    message text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_offer_amount_positive CHECK ((offer_amount > (0)::numeric)),
    CONSTRAINT chk_status_valid CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying, 'REJECTED'::character varying, 'OUTBID'::character varying])::text[])))
);


ALTER TABLE public.offers OWNER TO postgres;

--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE offers; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.offers IS 'Stores user offers/bids for collectible items';


--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN offers.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.offers.status IS 'Offer status: PENDING, ACCEPTED, REJECTED, OUTBID';


--
-- TOC entry 219 (class 1259 OID 57454)
-- Name: offers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.offers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.offers_id_seq OWNER TO postgres;

--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 219
-- Name: offers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.offers_id_seq OWNED BY public.offers.id;


--
-- TOC entry 216 (class 1259 OID 57432)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    full_name character varying(100) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 57431)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 3298 (class 2604 OID 57446)
-- Name: items id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.items ALTER COLUMN id SET DEFAULT nextval('public.items_id_seq'::regclass);


--
-- TOC entry 3302 (class 2604 OID 57458)
-- Name: offers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers ALTER COLUMN id SET DEFAULT nextval('public.offers_id_seq'::regclass);


--
-- TOC entry 3295 (class 2604 OID 57435)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3478 (class 0 OID 57443)
-- Dependencies: 218
-- Data for Name: items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.items (id, name, description, price, current_price, original_price, available, created_at, updated_at) FROM stdin;
1	Gorra autografiada por Peso Pluma	Una gorra autografiada por el famoso Peso Pluma.	621.34	650.00	621.34	t	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
2	Casco autografiado por Rosalía	Un casco autografiado por la famosa cantante Rosalía, una verdadera MOTOMAMI!	734.57	750.00	734.57	t	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
3	Chamarra de Bad Bunny	Una chamarra de la marca favorita de Bad Bunny, autografiada por el propio artista.	521.89	750.00	521.89	t	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
4	Guitarra de Fernando Delgadillo	Una guitarra acústica de alta calidad utilizada por el famoso cantautor Fernando Delgadillo.	823.12	900.00	823.12	t	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
5	Jersey firmado por Snoop Dogg	Un jersey autografiado por el legendario rapero Snoop Dogg.	355.67	355.67	355.67	t	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
6	Prenda de Cardi B autografiada	Un crop-top usado y autografiado por la famosa rapera Cardi B. en su última visita a México	674.23	674.23	674.23	t	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
7	Guitarra autografiada por Coldplay	Una guitarra eléctrica autografiada por la popular banda británica Coldplay, un día antes de su concierto en Monterrey en 2022.	458.91	458.91	458.91	t	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
\.


--
-- TOC entry 3480 (class 0 OID 57455)
-- Dependencies: 220
-- Data for Name: offers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offers (id, item_id, user_id, offer_amount, status, message, created_at, updated_at) FROM stdin;
1	1	1	650.00	PENDING	Me encanta Peso Pluma, ofrezco más del precio inicial!	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
2	1	2	750.00	PENDING	Ofrezco más para llevarme esta gorra única!	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
3	2	3	750.00	PENDING	Fan de Rosalía, quiero este casco!	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
4	3	1	550.00	PENDING	Gran precio por esta chamarra de Bad Bunny	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
5	4	4	900.00	PENDING	Soy coleccionista de instrumentos musicales	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
\.


--
-- TOC entry 3476 (class 0 OID 57432)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, email, full_name, created_at, updated_at) FROM stdin;
1	rafael	rafael@example.com	Rafael García	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
2	sofia	sofia@example.com	Sofía Martínez	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
3	ramon	ramon@example.com	Ramón Collector	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
4	arturo	arturo@example.com	Arturo Bandini	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
5	maria	maria@example.com	María López	2025-11-03 10:32:57.166994	2025-11-03 10:32:57.166994
\.


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 217
-- Name: items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.items_id_seq', 7, true);


--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 219
-- Name: offers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.offers_id_seq', 5, true);


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 5, true);


--
-- TOC entry 3321 (class 2606 OID 57453)
-- Name: items items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_pkey PRIMARY KEY (id);


--
-- TOC entry 3329 (class 2606 OID 57467)
-- Name: offers offers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_pkey PRIMARY KEY (id);


--
-- TOC entry 3312 (class 2606 OID 57439)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3314 (class 2606 OID 57441)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3315 (class 1259 OID 57484)
-- Name: idx_items_available; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_available ON public.items USING btree (available);


--
-- TOC entry 3316 (class 1259 OID 57485)
-- Name: idx_items_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_created_at ON public.items USING btree (created_at);


--
-- TOC entry 3317 (class 1259 OID 57483)
-- Name: idx_items_current_price; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_current_price ON public.items USING btree (current_price);


--
-- TOC entry 3318 (class 1259 OID 57481)
-- Name: idx_items_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_name ON public.items USING btree (name);


--
-- TOC entry 3319 (class 1259 OID 57482)
-- Name: idx_items_price; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_items_price ON public.items USING btree (price);


--
-- TOC entry 3322 (class 1259 OID 57489)
-- Name: idx_offers_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_offers_created_at ON public.offers USING btree (created_at);


--
-- TOC entry 3323 (class 1259 OID 57486)
-- Name: idx_offers_item_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_offers_item_id ON public.offers USING btree (item_id);


--
-- TOC entry 3324 (class 1259 OID 57490)
-- Name: idx_offers_item_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_offers_item_status ON public.offers USING btree (item_id, status);


--
-- TOC entry 3325 (class 1259 OID 57488)
-- Name: idx_offers_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_offers_status ON public.offers USING btree (status);


--
-- TOC entry 3326 (class 1259 OID 57487)
-- Name: idx_offers_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_offers_user_id ON public.offers USING btree (user_id);


--
-- TOC entry 3327 (class 1259 OID 57491)
-- Name: idx_offers_user_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_offers_user_status ON public.offers USING btree (user_id, status);


--
-- TOC entry 3308 (class 1259 OID 57480)
-- Name: idx_users_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_created_at ON public.users USING btree (created_at);


--
-- TOC entry 3309 (class 1259 OID 57479)
-- Name: idx_users_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_email ON public.users USING btree (email);


--
-- TOC entry 3310 (class 1259 OID 57478)
-- Name: idx_users_username; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_username ON public.users USING btree (username);


--
-- TOC entry 3330 (class 2606 OID 57468)
-- Name: offers offers_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_item_id_fkey FOREIGN KEY (item_id) REFERENCES public.items(id) ON DELETE CASCADE;


--
-- TOC entry 3331 (class 2606 OID 57473)
-- Name: offers offers_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


-- Completed on 2025-11-03 12:01:32 CST

--
-- PostgreSQL database dump complete
--

\unrestrict 0dnFfHfGQVOw7jKmB8S5VBixC0wpXhbgh346wf1k9RGodZsd0KWeQX5y42eCmuF

