import React, { Component } from "react";
import Typography from "@mui/material/Typography";

export default class Success extends Component {
  continue = async (e) => {
    e.preventDefault();
    // PROCESS FORM //
    this.props.nextStep();
  };

  back = (e) => {
    e.preventDefault();
    this.props.prevStep();
  };

  render() {
    const values = this.props;

    return (
      <div>
        <Typography variant="h5" align="center">
          Thank You {values.name} For Your Submission!
        </Typography>
        <br />
      </div>
    );
  }
}
