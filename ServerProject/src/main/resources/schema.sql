--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2
-- Dumped by pg_dump version 12.2

-- Started on 2022-03-25 20:25:12

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

--
-- TOC entry 217 (class 1255 OID 71498)
-- Name: delete_exception_events_after_delete_exception(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.delete_exception_events_after_delete_exception() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
   DELETE FROM events WHERE id = old.event_id;
   return old;
END;$$;


ALTER FUNCTION public.delete_exception_events_after_delete_exception() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 212 (class 1259 OID 63571)
-- Name: events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.events (
    id bigint NOT NULL,
    author_id bigint NOT NULL,
    title character varying(255) NOT NULL,
    location character varying(255),
    description character varying(255),
    start_date date NOT NULL,
    end_date date NOT NULL,
    alert_date date NOT NULL,
    start_time time(0) without time zone NOT NULL,
    end_time time(0) without time zone NOT NULL,
    alert_time time(0) without time zone NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT events_type_check CHECK (((type)::text = ANY ((ARRAY['Free time'::character varying, 'Work'::character varying, 'School'::character varying, 'Others'::character varying])::text[])))
);


ALTER TABLE public.events OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 63569)
-- Name: events_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.events_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.events_id_seq OWNER TO postgres;

--
-- TOC entry 2914 (class 0 OID 0)
-- Dependencies: 211
-- Name: events_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.events_id_seq OWNED BY public.events.id;


--
-- TOC entry 216 (class 1259 OID 63602)
-- Name: exceptions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exceptions (
    id bigint NOT NULL,
    event_id bigint,
    repetition_id bigint NOT NULL,
    date date NOT NULL
);


ALTER TABLE public.exceptions OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 63600)
-- Name: exceptions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.exceptions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.exceptions_id_seq OWNER TO postgres;

--
-- TOC entry 2915 (class 0 OID 0)
-- Dependencies: 215
-- Name: exceptions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.exceptions_id_seq OWNED BY public.exceptions.id;


--
-- TOC entry 208 (class 1259 OID 63543)
-- Name: failed_jobs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.failed_jobs (
    id bigint NOT NULL,
    uuid character varying(255) NOT NULL,
    connection text NOT NULL,
    queue text NOT NULL,
    payload text NOT NULL,
    exception text NOT NULL,
    failed_at timestamp(0) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.failed_jobs OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 63541)
-- Name: failed_jobs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.failed_jobs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.failed_jobs_id_seq OWNER TO postgres;

--
-- TOC entry 2916 (class 0 OID 0)
-- Dependencies: 207
-- Name: failed_jobs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.failed_jobs_id_seq OWNED BY public.failed_jobs.id;


--
-- TOC entry 203 (class 1259 OID 63517)
-- Name: migrations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.migrations (
    id integer NOT NULL,
    migration character varying(255) NOT NULL,
    batch integer NOT NULL
);


ALTER TABLE public.migrations OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 63515)
-- Name: migrations_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.migrations_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.migrations_id_seq OWNER TO postgres;

--
-- TOC entry 2917 (class 0 OID 0)
-- Dependencies: 202
-- Name: migrations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.migrations_id_seq OWNED BY public.migrations.id;


--
-- TOC entry 206 (class 1259 OID 63534)
-- Name: password_resets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.password_resets (
    email character varying(255) NOT NULL,
    token character varying(255) NOT NULL,
    created_at timestamp(0) without time zone
);


ALTER TABLE public.password_resets OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 63557)
-- Name: personal_access_tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.personal_access_tokens (
    id bigint NOT NULL,
    tokenable_type character varying(255) NOT NULL,
    tokenable_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    token character varying(64) NOT NULL,
    abilities text,
    last_used_at timestamp(0) without time zone,
    created_at timestamp(0) without time zone,
    updated_at timestamp(0) without time zone
);


ALTER TABLE public.personal_access_tokens OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 63555)
-- Name: personal_access_tokens_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.personal_access_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.personal_access_tokens_id_seq OWNER TO postgres;

--
-- TOC entry 2918 (class 0 OID 0)
-- Dependencies: 209
-- Name: personal_access_tokens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.personal_access_tokens_id_seq OWNED BY public.personal_access_tokens.id;


--
-- TOC entry 214 (class 1259 OID 63588)
-- Name: repetitions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.repetitions (
    id bigint NOT NULL,
    event_id bigint NOT NULL,
    start date NOT NULL,
    "end" date NOT NULL,
    repeat_interval integer NOT NULL,
    days_of_week integer,
    day_of_month integer,
    repeat_ordinal integer,
    month integer,
    type character varying(255) NOT NULL,
    CONSTRAINT repetitions_type_check CHECK (((type)::text = ANY ((ARRAY['Daily'::character varying, 'Weekly'::character varying, 'Monthly'::character varying, 'Yearly'::character varying])::text[])))
);


ALTER TABLE public.repetitions OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 63586)
-- Name: repetitions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.repetitions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.repetitions_id_seq OWNER TO postgres;

--
-- TOC entry 2919 (class 0 OID 0)
-- Dependencies: 213
-- Name: repetitions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.repetitions_id_seq OWNED BY public.repetitions.id;


--
-- TOC entry 205 (class 1259 OID 63525)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    firstname character varying(255) NOT NULL,
    lastname character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 63523)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 2920 (class 0 OID 0)
-- Dependencies: 204
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 2738 (class 2604 OID 63574)
-- Name: events id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events ALTER COLUMN id SET DEFAULT nextval('public.events_id_seq'::regclass);


--
-- TOC entry 2742 (class 2604 OID 63605)
-- Name: exceptions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exceptions ALTER COLUMN id SET DEFAULT nextval('public.exceptions_id_seq'::regclass);


--
-- TOC entry 2735 (class 2604 OID 63546)
-- Name: failed_jobs id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.failed_jobs ALTER COLUMN id SET DEFAULT nextval('public.failed_jobs_id_seq'::regclass);


--
-- TOC entry 2733 (class 2604 OID 63520)
-- Name: migrations id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.migrations ALTER COLUMN id SET DEFAULT nextval('public.migrations_id_seq'::regclass);


--
-- TOC entry 2737 (class 2604 OID 63560)
-- Name: personal_access_tokens id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personal_access_tokens ALTER COLUMN id SET DEFAULT nextval('public.personal_access_tokens_id_seq'::regclass);


--
-- TOC entry 2740 (class 2604 OID 63591)
-- Name: repetitions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repetitions ALTER COLUMN id SET DEFAULT nextval('public.repetitions_id_seq'::regclass);


--
-- TOC entry 2734 (class 2604 OID 63528)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 2904 (class 0 OID 63571)
-- Dependencies: 212
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.events (id, author_id, title, location, description, start_date, end_date, alert_date, start_time, end_time, alert_time, type) FROM stdin;
30	2	test			2022-01-22	2022-01-22	2022-01-22	13:30:00	14:00:00	11:45:00	Free time
46	3	test 2			2022-01-06	2022-01-06	2022-01-06	20:30:00	21:00:00	20:15:00	Free time
\.


--
-- TOC entry 2908 (class 0 OID 63602)
-- Dependencies: 216
-- Data for Name: exceptions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exceptions (id, event_id, repetition_id, date) FROM stdin;
\.


--
-- TOC entry 2900 (class 0 OID 63543)
-- Dependencies: 208
-- Data for Name: failed_jobs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.failed_jobs (id, uuid, connection, queue, payload, exception, failed_at) FROM stdin;
\.


--
-- TOC entry 2895 (class 0 OID 63517)
-- Dependencies: 203
-- Data for Name: migrations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.migrations (id, migration, batch) FROM stdin;
1	2014_10_12_000000_create_users_table	1
2	2014_10_12_100000_create_password_resets_table	1
3	2019_08_19_000000_create_failed_jobs_table	1
4	2019_12_14_000001_create_personal_access_tokens_table	1
5	2021_09_15_155611_create_events_table	1
6	2021_09_15_155635_create_repetitions_table	1
7	2021_09_15_155700_create_exceptions_table	1
10	2021_09_15_183205_create_trigger	2
\.


--
-- TOC entry 2898 (class 0 OID 63534)
-- Dependencies: 206
-- Data for Name: password_resets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.password_resets (email, token, created_at) FROM stdin;
\.


--
-- TOC entry 2902 (class 0 OID 63557)
-- Dependencies: 210
-- Data for Name: personal_access_tokens; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.personal_access_tokens (id, tokenable_type, tokenable_id, name, token, abilities, last_used_at, created_at, updated_at) FROM stdin;
\.


--
-- TOC entry 2906 (class 0 OID 63588)
-- Dependencies: 214
-- Data for Name: repetitions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.repetitions (id, event_id, start, "end", repeat_interval, days_of_week, day_of_month, repeat_ordinal, month, type) FROM stdin;
\.


--
-- TOC entry 2897 (class 0 OID 63525)
-- Dependencies: 205
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, firstname, lastname, username, password) FROM stdin;
2	Test first name	Test last name	username	$2a$10$ckNt.HrvayTiCUaXa5lul.ENWjR9h.nf1WUjRohkqyfbCjaiuQxT2
3	test	test	test	$2a$10$yxdmGkCeQ.S38rjbHZd8cuXEkW41U0dLMIm.Nsfj4Dosfln8MzbqG
4	test	test	test1	$2a$10$G0TF1VuGS7AhxEggXUgmquggYD.xj9.HNabSOutSvOm52QGG9VMXa
\.


--
-- TOC entry 2921 (class 0 OID 0)
-- Dependencies: 211
-- Name: events_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.events_id_seq', 66, true);


--
-- TOC entry 2922 (class 0 OID 0)
-- Dependencies: 215
-- Name: exceptions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.exceptions_id_seq', 32, true);


--
-- TOC entry 2923 (class 0 OID 0)
-- Dependencies: 207
-- Name: failed_jobs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.failed_jobs_id_seq', 1, false);


--
-- TOC entry 2924 (class 0 OID 0)
-- Dependencies: 202
-- Name: migrations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.migrations_id_seq', 10, true);


--
-- TOC entry 2925 (class 0 OID 0)
-- Dependencies: 209
-- Name: personal_access_tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.personal_access_tokens_id_seq', 1, false);


--
-- TOC entry 2926 (class 0 OID 0)
-- Dependencies: 213
-- Name: repetitions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.repetitions_id_seq', 36, true);


--
-- TOC entry 2927 (class 0 OID 0)
-- Dependencies: 204
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 4, true);


--
-- TOC entry 2758 (class 2606 OID 63580)
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);


--
-- TOC entry 2762 (class 2606 OID 63607)
-- Name: exceptions exceptions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exceptions
    ADD CONSTRAINT exceptions_pkey PRIMARY KEY (id);


--
-- TOC entry 2749 (class 2606 OID 63552)
-- Name: failed_jobs failed_jobs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.failed_jobs
    ADD CONSTRAINT failed_jobs_pkey PRIMARY KEY (id);


--
-- TOC entry 2751 (class 2606 OID 63554)
-- Name: failed_jobs failed_jobs_uuid_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.failed_jobs
    ADD CONSTRAINT failed_jobs_uuid_unique UNIQUE (uuid);


--
-- TOC entry 2744 (class 2606 OID 63522)
-- Name: migrations migrations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.migrations
    ADD CONSTRAINT migrations_pkey PRIMARY KEY (id);


--
-- TOC entry 2753 (class 2606 OID 63565)
-- Name: personal_access_tokens personal_access_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personal_access_tokens
    ADD CONSTRAINT personal_access_tokens_pkey PRIMARY KEY (id);


--
-- TOC entry 2755 (class 2606 OID 63568)
-- Name: personal_access_tokens personal_access_tokens_token_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personal_access_tokens
    ADD CONSTRAINT personal_access_tokens_token_unique UNIQUE (token);


--
-- TOC entry 2760 (class 2606 OID 63594)
-- Name: repetitions repetitions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repetitions
    ADD CONSTRAINT repetitions_pkey PRIMARY KEY (id);


--
-- TOC entry 2746 (class 2606 OID 63533)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2747 (class 1259 OID 63540)
-- Name: password_resets_email_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX password_resets_email_index ON public.password_resets USING btree (email);


--
-- TOC entry 2756 (class 1259 OID 63566)
-- Name: personal_access_tokens_tokenable_type_tokenable_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX personal_access_tokens_tokenable_type_tokenable_id_index ON public.personal_access_tokens USING btree (tokenable_type, tokenable_id);


--
-- TOC entry 2767 (class 2620 OID 71500)
-- Name: exceptions delete_exception_events_after_delete_exception; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER delete_exception_events_after_delete_exception AFTER DELETE ON public.exceptions FOR EACH ROW EXECUTE FUNCTION public.delete_exception_events_after_delete_exception();


--
-- TOC entry 2763 (class 2606 OID 63581)
-- Name: events events_author_id_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_author_id_foreign FOREIGN KEY (author_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 2765 (class 2606 OID 63608)
-- Name: exceptions exceptions_event_id_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exceptions
    ADD CONSTRAINT exceptions_event_id_foreign FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE SET NULL;


--
-- TOC entry 2766 (class 2606 OID 63613)
-- Name: exceptions exceptions_repetition_id_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exceptions
    ADD CONSTRAINT exceptions_repetition_id_foreign FOREIGN KEY (repetition_id) REFERENCES public.repetitions(id) ON DELETE CASCADE;


--
-- TOC entry 2764 (class 2606 OID 63595)
-- Name: repetitions repetitions_event_id_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repetitions
    ADD CONSTRAINT repetitions_event_id_foreign FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE;


-- Completed on 2022-03-25 20:25:14

--
-- PostgreSQL database dump complete
--

