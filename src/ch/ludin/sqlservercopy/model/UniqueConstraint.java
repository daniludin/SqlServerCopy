package ch.ludin.sqlservercopy.model;

import java.util.ArrayList;
import java.util.List;

public  class UniqueConstraint {
    public String table;
    public String name;
    public List<String> columns = new ArrayList<>();
   
    public UniqueConstraint() {
		super();
	}


	public String toString() {
        return String.format("[%s] %s: %s", table, name, columns);
    }
}

