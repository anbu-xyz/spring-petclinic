@import uk.anbu.spring.sample.petclinic.dto.PetDto
@import uk.anbu.spring.sample.petclinic.dto.OwnerDto
@import java.util.List

@param PetDto pet
@param OwnerDto owner
@param List<String> types
@param boolean isNew

<!DOCTYPE html>
<html>
<head>
    <title>Pet</title>
</head>
<body>
<h2>
    @if(isNew)
        New
    @endif
    Pet
</h2>
<form action="${isNew ? "/owners/" + owner.getEid() + "/pets/new" : "/owners/" + owner.getEid() + "/pets/" + pet.getEid() + "/edit"}"
      method="post">
    <input type="hidden" name="eid" value="${pet.getEid()}">
    <div class="form-group has-feedback">
        <div class="form-group">
            <label class="col-sm-2 control-label">Owner</label>
            <div class="col-sm-10">
                <span>${owner.getFirstName()} ${owner.getLastName()}</span>
            </div>
        </div>
        <input type="text" name="name" value="${pet.getName()}" placeholder="Name" required>
        <input type="date" name="birthDate" value="${pet.getBirthDate().toString()}" required>
        <select name="type" required>
            @for(String type : types)
                <option value="${type}" ${pet.getType().toString().equals(type) ? "selected" : ""}>${type}</option>
            @endfor
        </select>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button class="btn btn-primary" type="submit">
                ${isNew ? "Add Pet" : "Update Pet"}
            </button>
        </div>
    </div>
</form>
</body>
</html>
