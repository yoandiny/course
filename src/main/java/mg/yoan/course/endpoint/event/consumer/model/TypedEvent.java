package mg.yoan.course.endpoint.event.consumer.model;

import mg.yoan.course.PojaGenerated;
import mg.yoan.course.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
