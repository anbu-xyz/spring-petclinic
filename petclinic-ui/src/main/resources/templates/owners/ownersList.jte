@import uk.anbu.spring.sample.petclinic.dto.OwnerDto
@import uk.anbu.spring.sample.petclinic.dto.PetDto
@import java.util.List

@param List<OwnerDto> listOwners
@param int currentPage
@param int totalPages

<!DOCTYPE html>
<html>
<head>
    <title>Owners</title>
</head>
<body>
<h2>Owners</h2>

<table id="owners" class="table table-striped">
    <thead>
    <tr>
        <th style="width: 150px;">Name</th>
        <th style="width: 200px;">Address</th>
        <th>City</th>
        <th style="width: 120px">Telephone</th>
        <th>Pets</th>
    </tr>
    </thead>
    <tbody>
    @for(OwnerDto owner : listOwners)
        <tr>
            <td>
                <a href="/owners/${owner.getEid()}">${owner.getFirstName()} ${owner.getLastName()}</a>
            </td>
            <td>${owner.getAddress()}</td>
            <td>${owner.getCity()}</td>
            <td>${owner.getTelephone()}</td>
            <td>${String.join(", ", owner.getPets().stream().map(PetDto::getName).toList())}</td>
        </tr>
    @endfor
    </tbody>
</table>

@if(totalPages > 1)
    <div>
        <span>Pages:</span>
        <span>[</span>
        @for(int i = 1; i <= totalPages; i++)
            @if(currentPage != i)
                <a href="/owners?page=${i}">${i}</a>
            @else
                <span>${i}</span>
            @endif
        @endfor
        <span>]</span>
        <!-- Pagination controls (First, Previous, Next, Last) can be added here -->
    </div>
@endif
</body>
</html>
>
