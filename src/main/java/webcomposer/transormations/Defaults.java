package webcomposer.transormations;

import com.google.common.collect.ImmutableSet;
import io.crm.transformation.impl.json.object.IncludeExcludeTransformation;
import webcomposer.Model;

/**
 * Created by shahadat on 5/3/16.
 */
public class Defaults {
    public static final IncludeExcludeTransformation
        ActivityTrackingExcludeTransformation = new IncludeExcludeTransformation(null,
        ImmutableSet.of(Model.CREATED_BY, Model.UPDATED_BY, Model.CREATE_DATE, Model.UPDATE_DATE)
    );
}
