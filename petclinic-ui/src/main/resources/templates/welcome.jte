@param String welcomeMessage

@template.fragments.layout(
content = @`
    <h2>${welcomeMessage}</h2>
    <div class="row">
        <div class="col-md-12">
            <img class="img-responsive" src="/resources/images/pets.png" alt="Pets"/>
        </div>
    </div>
`,
activeMenu = "home"
)
