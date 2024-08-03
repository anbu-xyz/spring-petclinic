package uk.anbu.spring.sample.petclinic.lib.db;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class DataSourceWrapper implements DataSource {
	public final DataSource ds;

	@Override
	public Connection getConnection() throws SQLException {
		return new ConnectionWrapper(ds.getConnection());
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return new ConnectionWrapper(ds.getConnection(username, password));
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return ds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		ds.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		ds.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return ds.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return ds.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return ds.isWrapperFor(iface);
	}
}
