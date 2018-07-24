package com.cardinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A datasource which tracks the Tools for the store.
 */
public class ToolCatalogDto {

    /**
     * Retrieve all tools that can be rented from the catalog.
     *
     * @return the available tools to rent
     */
    public List<Tool> fetchToolsFromCatalog() {
        List<Tool> availableTools = new ArrayList<>();
        availableTools.add(new Tool("LADW", "Werner", ToolType.LADDER));
        availableTools.add(new Tool("CHNS", "Stihl", ToolType.CHAINSAW));
        availableTools.add(new Tool("JAKR", "Ridgid", ToolType.JACKHAMMER));
        availableTools.add(new Tool("JAKD", "DeWalt", ToolType.JACKHAMMER));
        return availableTools;
    }

    /**
     * Fetch a tool by its tool code.  If it is not found, Optional.empty() will be returned.
     *
     * @param toolCode the tool code to retrieve from the catalog
     * @return the tool, or empty if not found
     */
    public Optional<Tool> fetchToolByCode(String toolCode) {
        // Tool Code is likely our Primary Key, as there is no mention of quantity or a reserving system.
        // if we want to extend that, this will need to be adjusted.
        return fetchToolsFromCatalog().stream()
                .filter(tool -> Objects.equals(tool.getToolCode(), toolCode))
                .findAny();

    }
}
