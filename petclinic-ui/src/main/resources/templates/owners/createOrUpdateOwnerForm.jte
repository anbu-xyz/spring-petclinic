@import uk.anbu.spring.sample.petclinic.dto.OwnerDto

@param OwnerDto owner
@param boolean isNew

<!DOCTYPE html>
<html>
<head>
  <title>Owner</title>
</head>
<body>
<h2>Owner</h2>
<form action="${isNew ? "/owners/new" : "/owners/" + owner.getEid() + "/edit"}" method="post">
  <div class="form-group has-feedback">
    <input type="text" name="firstName" value="${owner.getFirstName()}" placeholder="First Name" required>
    <input type="text" name="lastName" value="${owner.getLastName()}" placeholder="Last Name" required>
    <input type="text" name="address" value="${owner.getAddress()}" placeholder="Address" required>
    <input type="text" name="city" value="${owner.getCity()}" placeholder="City" required>
    <input type="text" name="telephone" value="${owner.getTelephone()}" placeholder="Telephone" required>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button class="btn btn-primary" type="submit">
        ${isNew ? "Add Owner" : "Update Owner"}
      </button>
    </div>
  </div>
</form>
</body>
