import React, { useState, useEffect } from "react";
import {
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  ListItemAvatar,
  Avatar,
  TextField,
  IconButton,
  Container,
  Paper,
} from "@mui/material";
import Typography from "@mui/material/Typography";
import EditIcon from "@mui/icons-material/Edit";
import AssignmentIcon from "@mui/icons-material/Assignment";
import DeleteIcon from "@mui/icons-material/Delete";
import { UpdateForm } from "../Contactform/UpdateForm";

function TaskList({ task, fetchTasks }) {
  const [open, setOpen] = useState(false);
  const handleDelete = async (id) => {
    console.log(id);
    await fetch(`http://localhost:8080/task/delete/${id}`, {
      method: "DELETE",
    }).then((data) => {
      console.log(data);
      fetchTasks();
    });
  };

  return (
    <React.Fragment key={task.id}>
      <ListItem
        style={{
          marginBottom: 5,
          backgroundColor: task.synced ? "#e0ffe0" : "#ffe0e0",
          borderRadius: "4px",
        }}
      >
        <ListItemAvatar>
          <Avatar>
            <AssignmentIcon style={{ color: "#ff6f00" }} />{" "}
            {/* Orange color for the Assignment Icon */}
          </Avatar>
        </ListItemAvatar>
        <ListItemText
          primary={
            <Typography
              variant="h6"
              style={{
                textDecoration: task.isCompleted ? "line-through" : "none",
              }}
            >
              {task.title}
            </Typography>
          }
          secondary={
            <>
              <Typography component="span" variant="body2" color="textPrimary">
                {task.description}
              </Typography>
              <br />
              <Typography component="span" variant="body2">
                {`Start: ${task.startDate}`}
              </Typography>
              <br />
              <Typography component="span" variant="body2">
                {`End: ${task.dueDate}`}
              </Typography>
            </>
          }
        />
        <ListItemSecondaryAction>
          <IconButton
            edge="end"
            aria-label="update"
            onClick={() => setOpen(true)}
          >
            <EditIcon style={{ color: "#2962ff" }} />
          </IconButton>
          {open ? (
            <UpdateForm openDialog={open} setOpenDialog={setOpen} task={task} />
          ) : (
            <></>
          )}
          <IconButton
            edge="end"
            aria-label="delete"
            onClick={() => handleDelete(task.id)}
          >
            <DeleteIcon style={{ color: "#d32f2f" }} />
          </IconButton>
        </ListItemSecondaryAction>
      </ListItem>
    </React.Fragment>
  );
}

export default function TaskApp() {
  const [value, setValue] = useState("");

  const [tasks, setTasks] = useState([]);
  const fetchTasks = async () => {
    const response = await fetch("http://localhost:8080/task/getTasks", {
      method: "GET",
    });
    const data = await response.json();
    setTasks(data);
  };
  useEffect(() => {
    fetchTasks();
  }, []);

  // Filter tasks based on the search value
  const filteredTasks = tasks.filter(
    (task) =>
      task.title.toLowerCase().includes(value.toLowerCase()) ||
      task.description.toLowerCase().includes(value.toLowerCase())
  );

  return (
    <Container maxWidth="md">
      <TextField
        id="standard-bare"
        margin="normal"
        value={value}
        placeholder="Search Tasks..."
        onChange={(e) => setValue(e.target.value)}
      />
      <Paper>
        <List>
          {filteredTasks.map((task, index) => (
            <TaskList task={task} fetchTasks={fetchTasks} />
          ))}
        </List>
      </Paper>
    </Container>
  );
}
