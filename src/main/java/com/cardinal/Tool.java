package com.cardinal;

/**
 * A Tool available for rent.
 */
public class Tool {
    private String toolCode;
    private String brand;
    private ToolType toolType;

    public Tool(String toolCode, String brand, ToolType toolType) {
        this.toolCode = toolCode;
        this.brand = brand;
        this.toolType = toolType;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }
}
