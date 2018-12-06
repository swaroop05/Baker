package com.example.android.baker.data;

/**
 * Created by meets on 10/1/2018.
 */

public class BakingSteps {

    private String bakingStepsId;
    private String bakingStepsShortDescription;
    private String bakingStepsDescription;
    private String bakingStepsVideoUrl;
    private String bakingStepsThumbnailUrl;

    /**
     * Constructor to initialize the variables
     *
     * @param bakingStepsId
     * @param bakingStepsShortDescription
     * @param bakingStepsDescription
     * @param bakingStepsVideoUrl
     * @param bakingStepsThumbnailUrl
     */
    public BakingSteps(String bakingStepsId, String bakingStepsShortDescription, String bakingStepsDescription, String bakingStepsVideoUrl, String bakingStepsThumbnailUrl) {
        this.bakingStepsId = bakingStepsId;
        this.bakingStepsShortDescription = bakingStepsShortDescription;
        this.bakingStepsDescription = bakingStepsDescription;
        this.bakingStepsVideoUrl = bakingStepsVideoUrl;
        this.bakingStepsThumbnailUrl = bakingStepsThumbnailUrl;
    }

    public String getBakingStepsId() {
        return bakingStepsId;
    }

    public String getBakingStepsShortDescription() {
        return bakingStepsShortDescription;
    }

    public String getBakingStepsDescription() {
        return bakingStepsDescription;
    }

    public String getBakingStepsVideoUrl() {
        return bakingStepsVideoUrl;
    }

    public String getBakingStepsThumbnailUrl() {
        return bakingStepsThumbnailUrl;
    }
}
