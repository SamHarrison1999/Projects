package com.sg.classroster.dao;

public interface ClassRosterAuditDao {
    void writeAuditEntry(String entry) throws ClassRosterPersistenceException;
}
