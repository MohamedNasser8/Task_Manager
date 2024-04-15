import React, { useState } from "react";
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
} from "@mui/material";
export const UpdateForm = ({ openDialog, setOpenDialog, task }) => {
  const [open, setOpen] = useState(openDialog);
  const [title, setTitle] = useState(task.title);
  const [description, setDescription] = useState(task.description);
  const [day, setDay] = useState(task.startDate.split("T")[0]);
  const [startDate, setStartDate] = useState(task.startDate.split("T")[1]);

  const [endDate, setEndDate] = useState(task.dueDate.split("T")[1]);
  console.log(task);
  const canUpdate =
    title.length > 0 &&
    description.length > 0 &&
    startDate.length > 0 &&
    endDate.length > 0 &&
    startDate < endDate &&
    day.length > 0 &&
    (day !== task.startDate.split("T")[0] ||
      title !== task.title ||
      endDate !== task.dueDate.split("T")[1] ||
      description !== task.description ||
      startDate !== task.startDate.split("T")[1]);

  function handleClose() {
    setOpen(false);
    setOpenDialog(false);
  }
  function handleUpdate() {
    fetch(`http://localhost:8080/task/update/${task.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: title,
        description: description,
        day: day,
        startDate: startDate,
        dueDate: endDate,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        window.location.reload();
        console.log(data);
      });
  }

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      aria-labelledby="form-dialog-title"
    >
      <DialogTitle id="form-dialog-title">Update Task Information</DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          id="title"
          label="Title"
          type="text"
          fullWidth
          value={title}
          onChange={(e) => {
            e.preventDefault();
            setTitle(e.target.value);
          }}
        />
        <TextField
          margin="dense"
          id="description"
          label="Description"
          type="text"
          fullWidth
          multiline
          rows={4}
          value={description}
          onChange={(e) => {
            e.preventDefault();
            setDescription(e.target.value);
          }}
        />
        <TextField
          margin="dense"
          id="Day"
          label="Day"
          type="date"
          fullWidth
          value={day}
          InputLabelProps={{
            shrink: true,
          }}
          onChange={(e) => {
            e.preventDefault();
            setDay(e.target.value);
          }}
        />
        <TextField
          margin="dense"
          id="startDate"
          label="Start Date"
          type="time"
          fullWidth
          value={startDate}
          InputLabelProps={{
            shrink: true,
          }}
          onChange={(e) => {
            e.preventDefault();
            setStartDate(e.target.value);
          }}
        />

        <TextField
          margin="dense"
          id="endDate"
          label="End Date"
          type="time"
          fullWidth
          value={endDate}
          InputLabelProps={{
            shrink: true,
          }}
          onChange={(e) => {
            e.preventDefault();
            setEndDate(e.target.value);
          }}
        />
      </DialogContent>
      <DialogActions>
        {canUpdate ? (
          <Button color="success" onClick={handleUpdate}>
            Update
          </Button>
        ) : (
          <Button disabled>Update</Button>
        )}
        <Button onClick={handleClose} color="primary">
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
};
