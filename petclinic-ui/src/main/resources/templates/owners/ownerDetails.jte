@import uk.anbu.spring.sample.petclinic.dto.OwnerDto
@import uk.anbu.spring.sample.petclinic.dto.PetDto
@import uk.anbu.spring.sample.petclinic.dto.VisitDto

@param OwnerDto owner
@param String message
@param String error

<h2>Owner Information</h2>

@if(message != null)
    <div class="alert alert-success" id="success-message">
        <span>${message}</span>
    </div>
@endif

@if(error != null)
    <div class="alert alert-danger" id="error-message">
        <span>${error}</span>
    </div>
@endif

<table class="table table-striped">
    <tr>
        <th>Name</th>
        <td><b>${owner.getFirstName() + " " + owner.getLastName()}</b></td>
    </tr>
    <tr>
        <th>Address</th>
        <td>${owner.getAddress()}</td>
    </tr>
    <tr>
        <th>City</th>
        <td>${owner.getCity()}</td>
    </tr>
    <tr>
        <th>Telephone</th>
        <td>${owner.getTelephone()}</td>
    </tr>
</table>

<a href="/${owner.getEid()}/edit" class="btn btn-primary">Edit Owner</a>
<a href="/${owner.getEid()}/pets/new" class="btn btn-primary">Add New Pet</a>

<br />
<br />
<br />
<h2>Pets and Visits</h2>

<table class="table table-striped">
    @for(PetDto pet : owner.getPets())
        <tr>
            <td valign="top">
                <dl class="dl-horizontal">
                    <dt>Name</dt>
                    <dd>${pet.getName()}</dd>
                    <dt>Birth Date</dt>
                    <dd>${pet.getBirthDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</dd>
                    <dt>Type</dt>
                    <dd>${pet.getType().toString()}</dd>
                </dl>
            </td>
            <td valign="top">
                <table class="table-condensed">
                    <thead>
                    <tr>
                        <th>Visit Date</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    @for(VisitDto visit : pet.getVisits())
                        <tr>
                            <td>${visit.getVisitDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</td>
                            <td>${visit.getDescription()}</td>
                        </tr>
                    @endfor
                    <tr>
                        <td><a href="/${owner.getEid()}/pets/${pet.getEid()}/edit">Edit Pet</a></td>
                        <td><a href="/${owner.getEid()}/pets/${pet.getEid()}/visits/new">Add Visit</a></td>
                    </tr>
                </table>
            </td>
        </tr>
    @endfor
</table>

<script>
    function hideMessages() {
        setTimeout(function() {
            document.getElementById("success-message").style.display = "none";
            document.getElementById("error-message").style.display = "none";
        }, 3000);
    }
    hideMessages();
</script>
