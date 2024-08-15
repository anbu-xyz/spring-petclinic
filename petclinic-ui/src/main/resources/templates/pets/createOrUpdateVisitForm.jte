@import uk.anbu.spring.sample.petclinic.dto.OwnerDto
@import uk.anbu.spring.sample.petclinic.dto.PetDto
@import uk.anbu.spring.sample.petclinic.dto.VisitDto
@import java.util.List

@param VisitDto visit
@param PetDto pet
@param OwnerDto owner
@param List<VisitDto> visits

<h2>
    @if(visit.isNew())
        New
    @endif
    Visit
</h2>

<b>Pet</b>
<table class="table table-striped">
    <thead>
    <tr>
        <th>Name</th>
        <th>Birth Date</th>
        <th>Type</th>
        <th>Owner</th>
    </tr>
    </thead>
    <tr>
        <td>${pet.getName()}</td>
        <td>${pet.getBirthDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</td>
        <td>${pet.getType().toString()}</td>
        <td>${owner.getFirstName() + " " + owner.getLastName()}</td>
    </tr>
</table>

<form action="" method="post" class="form-horizontal">
    <div class="form-group has-feedback">
        <input type="date" id="date" name="date" value="${visit.getVisitDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))}" required />
        <input type="text" id="description" name="description" value="${visit.getDescription()}" required />
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input type="hidden" name="petId" value="${pet.getEid()}" />
            <button class="btn btn-primary" type="submit">Add Visit</button>
        </div>
    </div>
</form>

<br />
<b>Previous Visits</b>
<table class="table table-striped">
    <tr>
        <th>Date</th>
        <th>Description</th>
    </tr>
    @if(!visit.isNew())
        @for(VisitDto v : visits)
            <tr>
                <td>${v.getVisitDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</td>
                <td>${v.getDescription()}</td>
            </tr>
        @endfor
    @endif
</table>
