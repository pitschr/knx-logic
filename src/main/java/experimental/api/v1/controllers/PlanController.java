package experimental.api.v1.controllers;

import experimental.Plan;
import experimental.api.ComponentFactory;
import experimental.api.v1.json.CreateNewPlanRequest;
import experimental.api.v1.json.CreateNewPlanResponse;
import experimental.api.v1.json.PlanResponse;
import experimental.api.v1.json.SaveNoteRequest;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller about the plan containing components
 *
 * @author PITSCHR
 */
public class PlanController {
    private static final Logger LOG = LoggerFactory.getLogger(PlanController.class);

    private final ComponentFactory componentFactory;

    public PlanController(final ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    /**
     * Returns all plans
     *
     * @param ctx context
     */
    public void getPlans(final Context ctx) {
        LOG.trace("Get all plans");

        // returns json array of all plans with their ids

        ctx.status(200);
        ctx.json(List.of("1", "2", "3"));
    }

    /**
     * Returns info about a specific {@link Plan}
     *
     * @param ctx  context
     * @param plan the plan to be returned
     */
    public void getPlan(final Context ctx, final Plan plan) {
        LOG.trace("Get Info for Plan: {}", plan);

        // TODO: Check if plan exists
        // TODO: get individual plan (note, component ids)

        final var response = new PlanResponse();

        ctx.status(200);
        ctx.json(response);
    }

    /**
     * Creates a new plan based on {@link CreateNewPlanRequest}
     *
     * @param ctx     context
     * @param request the request object containing all information to create a new plan
     */
    public void createPlan(final Context ctx, final CreateNewPlanRequest request) {
        LOG.trace("Create New Plan: {}", request);

        // creates a new logic plan (return new id)  { name: "lorem ipsum", note: "lorem ipsum" }
        // TODO: create new plan
        final var response = new CreateNewPlanResponse();

        ctx.status(201);
        ctx.json(response);
    }

    /**
     * Deletes a specific Plan. This method is idempotent, if the plan
     * doesn't exists, no effect.
     *
     * @param ctx  context
     * @param plan the plan to be deleted
     */
    public void deletePlan(final Context ctx, final Plan plan) {
        LOG.trace("Delete for Plan: {}", plan);

        // TODO: check if plan exists
        // TODO: delete plan
        // deletes the plan (no content response)

        ctx.status(204);
    }

    /**
     * Updates the {@link Plan} with the note
     *
     * @param ctx     context
     * @param plan    the plan to be updated
     * @param request request with note to be saved
     */
    public void saveNote(final Context ctx, final Plan plan, final SaveNoteRequest request) {
        LOG.trace("Save Note for Plan '{}': {}", plan, request);

        // TODO: check if plan exists
        // TODO save note
        // saves note about logic plan               { text: "lorem ipsum" }

        ctx.status(201);
    }
}
