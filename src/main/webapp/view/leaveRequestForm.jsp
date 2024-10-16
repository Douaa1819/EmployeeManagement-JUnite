<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Leave Request Form</title>
    <link rel="stylesheet" href="styles.css"> <!-- Assurez-vous d'avoir un fichier CSS -->
</head>
<body class="dark-theme">
<div class="container">
    <h1>Request Leave</h1>

    <c:if test="${not empty message}">
        <div class="message">
            <p>${message}</p>
        </div>
    </c:if>

    <form action="leaveRequest" method="post" enctype="multipart/form-data">
        <input type="hidden" name="employeeId" value="3">

        <div class="form-group">
            <label for="startDate"><i class="icon-calendar"></i> Start Date</label>
            <input type="date" id="startDate" name="startDate" required>
        </div>

        <div class="form-group">
            <label for="endDate"><i class="icon-calendar"></i> End Date</label>
            <input type="date" id="endDate" name="endDate" required>
        </div>

        <div class="form-group">
            <label for="reason"><i class="icon-reason"></i> Reason</label>
            <textarea id="reason" name="reason" placeholder="Enter the reason for your leave" required></textarea>
        </div>

        <div class="form-group">
            <label for="certificate"><i class="icon-upload"></i> Upload Certificate (PDF)</label>
            <input type="file" id="certificate" name="certificate" accept="application/pdf" required>
        </div>

        <button type="submit"><i class="icon-submit"></i> Submit Request</button>
    </form>
</div>


<style>
    body.dark-theme {
        background-color: #121212;
        color: #fff;
        font-family: Arial, sans-serif;
    }

    .container {
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
        background-color: #1e1e1e;
        border-radius: 8px;
    }

    h1 {
        text-align: center;
    }

    .form-group {
        margin-bottom: 15px;
    }

    label {
        display: block;
        font-size: 1.2rem;
        margin-bottom: 5px;
    }

    input, textarea {
        width: 100%;
        padding: 10px;
        font-size: 1rem;
        color: #fff;
        background-color: #333;
        border: 1px solid #555;
        border-radius: 4px;
    }

    button {
        display: inline-block;
        padding: 10px 15px;
        font-size: 1rem;
        color: #fff;
        background-color: #0066cc;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    button:hover {
        background-color: #0055a5;
    }

    .icon-calendar, .icon-reason, .icon-upload, .icon-submit {
        margin-right: 5px;
    }

</style>
</body>
</html>
