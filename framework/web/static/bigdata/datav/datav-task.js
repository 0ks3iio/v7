(function (root) {
    let dataVTimer = {};

    let taskHolder = new Map();
    let taskUUIDHolder = new Map();

    dataVTimer.addTask = function (task, key, interval) {
        taskHolder.set(key, task);
        let taskUUID = setInterval(task, interval);
        taskUUIDHolder.set(key, taskUUID);
    };

    dataVTimer.removeTask = function (key) {
        let taskUUID = taskUUIDHolder.get(key);
        clearInterval(taskUUID);
        taskHolder.delete(key);
        taskUUIDHolder.delete(key);
    };

    dataVTimer.existsTask = function(key) {
        let task = taskHolder.get(key);
        if (task) {
            return true;
        } else {
            return false;
        }
    };

    dataVTimer.changeTaskInterval = function (key, interval) {
        let task = taskHolder.get(key);
        if (task) {
            this.removeTask(key);
            this.addTask(task, key, interval);
        }
    };
    root.dataVTimer = dataVTimer;
}(this));