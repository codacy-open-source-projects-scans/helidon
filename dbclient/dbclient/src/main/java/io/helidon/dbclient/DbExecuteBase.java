/*
 * Copyright (c) 2019, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.helidon.dbclient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Base {@link DbExecute} implementation.
 */
public abstract class DbExecuteBase implements DbExecute {

    private final DbClientContext context;

    /**
     * Create an instance of common database executor.
     *
     * @param context database client context
     */
    protected DbExecuteBase(DbClientContext context) {
        this.context = context;
    }

    /**
     * Return a statement text based on the statement name.
     * This is a utility method that probably would use {@link DbStatements} to retrieve the named statements.
     *
     * @param name name of the statement
     * @return statement text
     */
    protected String statementText(String name) {
        return context.statements().statement(name);
    }

    /**
     * Return database client context.
     *
     * @return database client context
     */
    protected DbClientContext context() {
        return context;
    }

    /**
     * Return database client context cast to it's extending class.
     *
     * @param cls {@link DbClientContext} extending class
     * @return extended client context
     * @param <C> client context extending type
     */
    protected <C extends DbClientContext> C context(Class<C> cls) {
        return cls.cast(context);
    }

    @Override
    public DbStatementQuery createNamedQuery(String statementName) {
        return createNamedQuery(statementName, statementText(statementName));
    }

    @Override
    public DbStatementQuery createQuery(String statement) {
        return createNamedQuery(generateName(DbStatementType.QUERY, statement), statement);
    }

    @Override
    public DbStatementGet createNamedGet(String statementName) {
        return createNamedGet(statementName, statementText(statementName));
    }

    @Override
    public DbStatementGet createGet(String statement) {
        return createNamedGet(generateName(DbStatementType.GET, statement), statement);
    }

    @Override
    public DbStatementDml createNamedInsert(String statementName) {
        return createNamedInsert(statementName, statementText(statementName));
    }

    @Override
    public DbStatementDml createInsert(String statement) {
        return createNamedInsert(generateName(DbStatementType.INSERT, statement), statement);
    }

    @Override
    public DbStatementDml createNamedUpdate(String statementName) {
        return createNamedUpdate(statementName, statementText(statementName));
    }

    @Override
    public DbStatementDml createUpdate(String statement) {
        return createNamedUpdate(generateName(DbStatementType.UPDATE, statement), statement);
    }

    @Override
    public DbStatementDml createNamedDelete(String statementName) {
        return createNamedDelete(statementName, statementText(statementName));
    }

    @Override
    public DbStatementDml createDelete(String statement) {
        return createNamedDelete(generateName(DbStatementType.DELETE, statement), statement);
    }

    @Override
    public DbStatementDml createNamedDmlStatement(String statementName) {
        return createNamedDmlStatement(statementName, statementText(statementName));
    }

    @Override
    public DbStatementDml createDmlStatement(String statement) {
        return createNamedDmlStatement(generateName(DbStatementType.DML, statement), statement);
    }

    /**
     * Generate a name for a statement.
     * The default implementation uses {@code SHA-256} so the same name is always
     * returned for the same statement.
     * <p>
     * As there is always a small risk of duplicity, named statements are recommended!
     *
     * @param type      type of the statement
     * @param statement statement that is going to be executed
     * @return name of the statement
     */
    protected String generateName(DbStatementType type, String statement) {
        String sha256;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(statement.getBytes(StandardCharsets.UTF_8));
            sha256 = Base64.getEncoder().encodeToString(digest.digest());
        } catch (NoSuchAlgorithmException ignored) {
            return "sha256failed";
        }
        return type.prefix() + '_' + sha256;
    }

}
