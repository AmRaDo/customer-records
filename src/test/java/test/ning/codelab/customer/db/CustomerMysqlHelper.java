/*

 * Copyright 2010-2013 Ning, Inc.

 *

 * Ning licenses this file to you under the Apache License, version 2.0

 * (the "License"); you may not use this file except in compliance with the

 * License.  You may obtain a copy of the License at:

 *

 *    http://www.apache.org/licenses/LICENSE-2.0

 *

 * Unless required by applicable law or agreed to in writing, software

 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT

 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the

 * License for the specific language governing permissions and limitations

 * under the License.

 */
package test.ning.codelab.customer.db;

import com.mysql.management.MysqldResource;
import com.mysql.management.MysqldResourceI;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.IDBI;
import org.skife.jdbi.v2.tweak.HandleCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class CustomerMysqlHelper
{
    private static final Logger log = LoggerFactory.getLogger(CustomerMysqlHelper.class);

    public final static String USERNAME = "root";
    public final static String PASSWORD = "root";

    private File dbDir;
    private MysqldResource mysqldResource;
    private int port = 0;

    public int getPort()
    {
        return port;
    }

    public void startMysql() throws IOException
    {
        ServerSocket socket = new ServerSocket(0);

        port = socket.getLocalPort();
        socket.close();

        dbDir = File.createTempFile("mysqldb", ".db");
        Assert.assertTrue(dbDir.delete());
        Assert.assertTrue(dbDir.mkdir());

        mysqldResource = new MysqldResource(dbDir);

        Map<String, String> dbOpts = new HashMap<String, String>();

        dbOpts.put(MysqldResourceI.PORT, Integer.toString(port));
        dbOpts.put(MysqldResourceI.INITIALIZE_USER, "true");
        dbOpts.put(MysqldResourceI.INITIALIZE_USER_NAME, USERNAME);
        dbOpts.put(MysqldResourceI.INITIALIZE_PASSWORD, PASSWORD);

        mysqldResource.start("test-mysqld-thread", dbOpts);

        if (!mysqldResource.isRunning()) {
            throw new IllegalStateException("MySQL did not start.");
        }
    }

    public void stopMysql()
    {
        mysqldResource.shutdown();
        FileUtils.deleteQuietly(dbDir);
    }

    public String getJdbcUrl()
    {
        final String jdbcUrl = "jdbc:mysql://localhost:" + port + "/Customer_db?createDatabaseIfNotExist=true&allowMultiQueries=true";
        return jdbcUrl;
    }

    public void initDb() throws IOException
    {
        final String jdbcUrl = getJdbcUrl();
        final IDBI dbi = new DBI(jdbcUrl, USERNAME, PASSWORD);
        final String ddl = IOUtils.toString(CustomerMysqlHelper.class.getResourceAsStream("/Customer_db.sql"));

        log.info(String.format("Creating embedded db for url %s", jdbcUrl));
        log.info("Adding tables to the embedded db");
        dbi.withHandle(new HandleCallback<Void>() {
            @Override
            public Void withHandle(Handle handle) throws Exception
            {
                handle.createScript(ddl).execute();
                return null;
            }
        });
        log.info("Finished adding tables to the embedded db");
        log.info("Finished initializing the embedded db");
    }
}
