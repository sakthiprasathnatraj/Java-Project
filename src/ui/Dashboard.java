package ui;

import ui.admin.AdminDashboard;
import ui.staff.StaffDashboard;

public class Dashboard {

    public Dashboard(String role){
        if (role.equals("Admin")) {
            new AdminDashboard(role);
        } else if(role.equals("Staff")){
            new StaffDashboard();
        }
    }
}
