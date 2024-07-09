package bean;

public class unloadingDataBean {
    private String panel_module_qty;
    private  String panel_values;
    private String pump_serial_no;
    private String motor_serial_no;
    private String controller_serial_no;
    private String material_status;
    private String remark;
    private String bill_no;

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public unloadingDataBean() {
    }

    public unloadingDataBean(String panel_module_qty, String panel_values, String pump_serial_no, String motor_serial_no, String controller_serial_no, String material_status, String remark, String bill_no) {
        this.panel_module_qty = panel_module_qty;
        this.panel_values = panel_values;
        this.pump_serial_no = pump_serial_no;
        this.motor_serial_no = motor_serial_no;
        this.controller_serial_no = controller_serial_no;
        this.material_status = material_status;
        this.remark = remark;
        this.bill_no = bill_no;
    }

    public String getPanel_module_qty() {
        return panel_module_qty;
    }

    public void setPanel_module_qty(String panel_module_qty) {
        this.panel_module_qty = panel_module_qty;
    }

    public String getPanel_values() {
        return panel_values;
    }

    public void setPanel_values(String panel_values) {
        this.panel_values = panel_values;
    }

    public String getPump_serial_no() {
        return pump_serial_no;
    }

    public void setPump_serial_no(String pump_serial_no) {
        this.pump_serial_no = pump_serial_no;
    }

    public String getMotor_serial_no() {
        return motor_serial_no;
    }

    public void setMotor_serial_no(String motor_serial_no) {
        this.motor_serial_no = motor_serial_no;
    }

    public String getController_serial_no() {
        return controller_serial_no;
    }

    public void setController_serial_no(String controller_serial_no) {
        this.controller_serial_no = controller_serial_no;
    }

    public String getMaterial_status() {
        return material_status;
    }

    public void setMaterial_status(String material_status) {
        this.material_status = material_status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "unloadingDataBean{" +
                "panel_module_qty='" + panel_module_qty + '\'' +
                ", panel_values='" + panel_values + '\'' +
                ", pump_serial_no='" + pump_serial_no + '\'' +
                ", motor_serial_no='" + motor_serial_no + '\'' +
                ", controller_serial_no='" + controller_serial_no + '\'' +
                ", material_status='" + material_status + '\'' +
                ", remark='" + remark + '\'' +
                ", bill_no='" + bill_no + '\'' +
                '}';
    }
}
