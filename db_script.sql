CREATE SEQUENCE files_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE files_id_seq
  OWNER TO postgres;

CREATE TABLE userfiles
(
  id smallint NOT NULL DEFAULT nextval('files_id_seq'::regclass),
  clientid character varying,
  filepath character varying,
  filename character varying,
  CONSTRAINT id PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE userfiles
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION register_file(
    clientid character varying,
    filepath character varying,
    filename character varying)
  RETURNS integer AS
$BODY$
DECLARE
    created_row_id INTEGER;
BEGIN
	created_row_id= 0;

	INSERT INTO userfiles(clientid,filepath,filename) VALUES (clientid,filepath,filename);

	SELECT max(id) FROM userfiles INTO created_row_id;

	RETURN created_row_id;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION register_file(character varying, character varying, character varying)
  OWNER TO postgres;


CREATE OR REPLACE FUNCTION get_fileslist(IN client_id character varying)
  RETURNS TABLE(fid smallint, fname character varying) AS
$BODY$
BEGIN
	RETURN QUERY SELECT id, filename FROM userfiles WHERE clientid=client_id;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION get_fileslist(character varying)
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION get_filepath(file_id integer)
  RETURNS character varying AS
$BODY$
DECLARE
    file_path VARCHAR;
BEGIN
	file_path= '';

	SELECT filepath 
	FROM userfiles 
	WHERE id=file_id
	INTO file_path;

	RETURN file_path;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_filepath(integer)
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION get_filename(file_id integer)
  RETURNS character varying AS
$BODY$
DECLARE
    file_name VARCHAR;
BEGIN
	file_name= '';

	SELECT filename 
	FROM userfiles 
	WHERE id=file_id
	INTO file_name;

	RETURN file_name;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_filename(integer)
  OWNER TO postgres;
