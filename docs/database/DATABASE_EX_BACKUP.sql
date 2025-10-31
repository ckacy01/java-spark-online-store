--
-- PostgreSQL database dump
--

\restrict qm5DmRNdiRt6rnl3HIO0u6c7loRgR9sxIa4ApgIGFjbiRmpagWNOw0hM7B3BeRS

-- Dumped from database version 16.10 (Ubuntu 16.10-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.10 (Ubuntu 16.10-0ubuntu0.24.04.1)

-- Started on 2025-10-31 11:03:58 CST

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
-- TOC entry 216 (class 1259 OID 32842)
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
-- TOC entry 215 (class 1259 OID 32841)
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
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 3285 (class 2604 OID 32845)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3439 (class 0 OID 32842)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, email, full_name, created_at, updated_at) FROM stdin;
1	rafael	rafael@example.com	Rafael García	2025-10-29 23:26:44.017555	2025-10-29 23:26:44.017555
2	sofia	sofia@example.com	Sofía Martínez	2025-10-29 23:26:44.017555	2025-10-29 23:26:44.017555
3	ramon	ramon@example.com	Ramón Collector	2025-10-29 23:26:44.017555	2025-10-29 23:26:44.017555
16	Jorgeav	babakas@gmail.com	JAJAJAA	2025-10-29 23:37:10.975458	2025-10-29 23:37:10.975493
17	Jorgeav1	babakas@gmail.com	JAJAJAA	2025-10-29 23:37:27.125053	2025-10-29 23:37:27.125074
\.


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 21, true);


--
-- TOC entry 3292 (class 2606 OID 32849)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3294 (class 2606 OID 32851)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3288 (class 1259 OID 32854)
-- Name: idx_users_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_created_at ON public.users USING btree (created_at);


--
-- TOC entry 3289 (class 1259 OID 32853)
-- Name: idx_users_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_email ON public.users USING btree (email);


--
-- TOC entry 3290 (class 1259 OID 32852)
-- Name: idx_users_username; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_username ON public.users USING btree (username);


-- Completed on 2025-10-31 11:03:58 CST

--
-- PostgreSQL database dump complete
--

\unrestrict qm5DmRNdiRt6rnl3HIO0u6c7loRgR9sxIa4ApgIGFjbiRmpagWNOw0hM7B3BeRS
