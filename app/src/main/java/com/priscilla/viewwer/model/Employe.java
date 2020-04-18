package com.priscilla.viewwer.model;

public class Employe {
    private String employee_name;
    private String employee_salary;

    public Employe() {
    }

    public Employe(String employee_name, String employee_salary) {
        this.employee_name = employee_name;
        this.employee_salary = employee_salary;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_salary() {
        return employee_salary;
    }

    public void setEmployee_salary(String employee_salary) {
        this.employee_salary = employee_salary;
    }

    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return contact_name
     */
    @Override
    public String toString() {
        return employee_name;
    }
}
