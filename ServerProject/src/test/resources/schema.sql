--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2
-- Dumped by pg_dump version 12.2

-- Started on 2020-08-26 19:57:23

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
-- TOC entry 8 (class 2615 OID 25907)
-- Name: planitschema; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA planitschema;


ALTER SCHEMA planitschema OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 206 (class 1259 OID 25921)
-- Name: event; Type: TABLE; Schema: planitschema; Owner: postgres
--

CREATE TABLE planitschema.event (
    idevent integer NOT NULL,
    title character varying NOT NULL,
    location character varying,
    date date NOT NULL,
    starts time without time zone NOT NULL,
    ends time without time zone,
    alert time without time zone,
    description character varying,
    ends_date date,
    alert_date date,
    type public.event_type
);


ALTER TABLE planitschema.event OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 25919)
-- Name: event_idevent_seq; Type: SEQUENCE; Schema: planitschema; Owner: postgres
--

CREATE SEQUENCE planitschema.event_idevent_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE planitschema.event_idevent_seq OWNER TO postgres;

--
-- TOC entry 2852 (class 0 OID 0)
-- Dependencies: 205
-- Name: event_idevent_seq; Type: SEQUENCE OWNED BY; Schema: planitschema; Owner: postgres
--

ALTER SEQUENCE planitschema.event_idevent_seq OWNED BY planitschema.event.idevent;


--
-- TOC entry 208 (class 1259 OID 27044)
-- Name: repetition; Type: TABLE; Schema: planitschema; Owner: postgres
--

CREATE TABLE planitschema.repetition (
    event_id bigint NOT NULL,
    starts date NOT NULL,
    ends date NOT NULL,
    repeat_interval integer NOT NULL,
    days_of_week integer,
    day_of_month integer,
    repeat_ordinal integer,
    month integer,
    type public.repetition_type NOT NULL
);


ALTER TABLE planitschema.repetition OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 25910)
-- Name: user; Type: TABLE; Schema: planitschema; Owner: postgres
--

CREATE TABLE planitschema."user" (
    iduser integer NOT NULL,
    firstname character varying,
    lastname character varying,
    username character varying,
    userpassword character varying,
    hashed boolean DEFAULT true
);


ALTER TABLE planitschema."user" OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 25908)
-- Name: user_iduser_seq; Type: SEQUENCE; Schema: planitschema; Owner: postgres
--

CREATE SEQUENCE planitschema.user_iduser_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE planitschema.user_iduser_seq OWNER TO postgres;

--
-- TOC entry 2853 (class 0 OID 0)
-- Dependencies: 203
-- Name: user_iduser_seq; Type: SEQUENCE OWNED BY; Schema: planitschema; Owner: postgres
--

ALTER SEQUENCE planitschema.user_iduser_seq OWNED BY planitschema."user".iduser;


--
-- TOC entry 207 (class 1259 OID 25930)
-- Name: userevent; Type: TABLE; Schema: planitschema; Owner: postgres
--

CREATE TABLE planitschema.userevent (
    iduser integer NOT NULL,
    idevent integer NOT NULL
);


ALTER TABLE planitschema.userevent OWNER TO postgres;

--
-- TOC entry 2711 (class 2604 OID 25924)
-- Name: event idevent; Type: DEFAULT; Schema: planitschema; Owner: postgres
--

ALTER TABLE ONLY planitschema.event ALTER COLUMN idevent SET DEFAULT nextval('planitschema.event_idevent_seq'::regclass);


--
-- TOC entry 2709 (class 2604 OID 25913)
-- Name: user iduser; Type: DEFAULT; Schema: planitschema; Owner: postgres
--

ALTER TABLE ONLY planitschema."user" ALTER COLUMN iduser SET DEFAULT nextval('planitschema.user_iduser_seq'::regclass);


--
-- TOC entry 2715 (class 2606 OID 25929)
-- Name: event event_pkey; Type: CONSTRAINT; Schema: planitschema; Owner: postgres
--

ALTER TABLE ONLY planitschema.event
    ADD CONSTRAINT event_pkey PRIMARY KEY (idevent);


--
-- TOC entry 2717 (class 2606 OID 27051)
-- Name: repetition repetition_pkey; Type: CONSTRAINT; Schema: planitschema; Owner: postgres
--

ALTER TABLE ONLY planitschema.repetition
    ADD CONSTRAINT repetition_pkey PRIMARY KEY (event_id);


--
-- TOC entry 2713 (class 2606 OID 25918)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: planitschema; Owner: postgres
--

ALTER TABLE ONLY planitschema."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (iduser);


--
-- TOC entry 2720 (class 2606 OID 27052)
-- Name: repetition repetition_event_id_fkey; Type: FK CONSTRAINT; Schema: planitschema; Owner: postgres
--

ALTER TABLE ONLY planitschema.repetition
    ADD CONSTRAINT repetition_event_id_fkey FOREIGN KEY (event_id) REFERENCES planitschema.event(idevent);


--
-- TOC entry 2719 (class 2606 OID 25996)
-- Name: userevent userevent_idevent_fkey; Type: FK CONSTRAINT; Schema: planitschema; Owner: postgres
--

ALTER TABLE ONLY planitschema.userevent
    ADD CONSTRAINT userevent_idevent_fkey FOREIGN KEY (idevent) REFERENCES planitschema.event(idevent);


--
-- TOC entry 2718 (class 2606 OID 25991)
-- Name: userevent userevent_iduser_fkey; Type: FK CONSTRAINT; Schema: planitschema; Owner: postgres
--

ALTER TABLE ONLY planitschema.userevent
    ADD CONSTRAINT userevent_iduser_fkey FOREIGN KEY (iduser) REFERENCES planitschema."user"(iduser);


-- Completed on 2020-08-26 19:57:23

--
-- PostgreSQL database dump complete
--

