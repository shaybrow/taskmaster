package com.shaybrow.taskmaster1;

import com.amplifyframework.api.ApiException;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.Consumer;
import com.amplifyframework.datastore.generated.model.Task;

public class TaskRepository {

    public void saveTask (Task task, Consumer success, Consumer fail){
        Amplify.API.mutate(ModelMutation.create(task), success, fail);
    }
    public void getTask (String id, Consumer<Task> success, Consumer<ApiException> fail){
        Amplify.API.query(ModelQuery.get(Task.class, id), r->{
            success.accept(r.getData());
        },
                fail);
    }
    public void deleteTask (Task task, Consumer success, Consumer fail){
        Amplify.API.mutate(ModelMutation.delete(task), success, fail);
    }
    public void updateTask (Task task, Consumer success, Consumer fail){
        Amplify.API.mutate(ModelMutation.update(task), success, fail);
    }
}
