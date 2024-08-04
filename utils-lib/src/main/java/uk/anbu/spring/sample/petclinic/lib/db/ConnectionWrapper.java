package uk.anbu.spring.sample.petclinic.lib.db;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
public class ConnectionWrapper implements Connection {
	private interface ExcludeDelegate {
		void close();
		void setAutoCommit(boolean autoCommit);
		void commit();
		void rollback();
	}

	@Delegate(excludes = ExcludeDelegate.class)
	private final Connection conn;

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		log.trace("autocommit = {}", autoCommit);
		conn.setAutoCommit(autoCommit);
	}

	@Override
	public void commit() throws SQLException {
		log.trace("commit issued");
		conn.commit();
	}

	@Override
	public void rollback() throws SQLException {
		log.trace("rollback issued");
		conn.commit();
	}

	@Override
	public void close() throws SQLException {
		log.trace("Closing connection for schema {}", conn.getSchema());
		conn.close();
	}

}
